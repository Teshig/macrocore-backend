package org.grimjo.macrocore.infrastructure.persistence.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.infrastructure.persistence.entity.statics.ContentVersionEntity;
import org.grimjo.macrocore.infrastructure.persistence.json.source.GenesisSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.json.source.NpcSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.json.source.PlayerSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.json.source.RoomSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.json.source.SettlementSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.json.source.ZoneSourceJson;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.NpcContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.PlayerContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.RoomContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.SettlementContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.mapper.statics.ZoneContentMapper;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.ContentVersionRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.NpcRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.PlayerRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.RoomRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.SettlementRepository;
import org.grimjo.macrocore.infrastructure.persistence.repository.statics.ZoneRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.DigestUtils;

@Slf4j
@Builder
@RequiredArgsConstructor
public class StaticPersistenceService {
  private static final String PATH_RESOURCE_ROOT = "/data/";
  private static final String PATH_ZONE = PATH_RESOURCE_ROOT + "zone/";

  private static final String FILE_GENESIS = "genesis.json";
  private static final String FILE_PLAYERS = "players.json";
  private static final String FILE_ZONE = "zone.json";
  private static final String FILE_ROOMS = "room.json";
  private static final String FILE_NPCS = "npc.json";
  private static final String FILE_SETTLEMENTS = "settlement.json";

  private final ContentVersionRepository versionRepository;
  private final ZoneRepository zoneRepository;
  private final RoomRepository roomRepository;
  private final NpcRepository npcRepository;
  private final PlayerRepository playerRepository;
  private final SettlementRepository settlementRepository;

  private final ZoneContentMapper zoneMapper;
  private final RoomContentMapper roomMapper;
  private final NpcContentMapper npcMapper;
  private final PlayerContentMapper playerMapper;
  private final SettlementContentMapper settlementMapper;

  private final ObjectMapper objectMapper;

  @PostConstruct
  @Transactional
  public void loadContent() {
    log.info("Checking Game Content for updates...");

    try {
      GenesisSourceJson manifest = loadResource(PATH_RESOURCE_ROOT + FILE_GENESIS, GenesisSourceJson.class);
      log.info("Found {} active zones.", manifest.getActiveZones().size());

      for (String zoneFileName : manifest.getActiveZones()) {
        processZone(zoneFileName);
      }
      
      loadAndProcessList(
          PATH_RESOURCE_ROOT + FILE_PLAYERS,
          PlayerSourceJson.class,
          playerSource -> playerRepository.save(playerMapper.toEntity(playerSource))
      );

    } catch (Exception e) {
      log.error("Critical error during content loading!", e);
      throw new RuntimeException("Content loading failed", e);
    }

    log.info("Game Content is up to date!");
  }

  @Transactional
  protected void processZone(String zoneName) {
    log.info("Processing zone: {}", zoneName);
    String basePath = PATH_ZONE + zoneName + "/";

    ZoneSourceJson zone = loadAndProcessSingleSource(
        basePath + FILE_ZONE,
        ZoneSourceJson.class,
        source -> zoneRepository.save(zoneMapper.toEntity(source))
    );

    if (zone == null) {
      log.error("Missing mandatory file {} in folder {}. Skipping zone.", FILE_ZONE, zoneName);
      return;
    }

    Long zoneId = zone.getId();

    loadAndProcessList(
        basePath + FILE_ROOMS,
        RoomSourceJson.class,
        roomSource -> roomRepository.save(roomMapper.toEntity(roomSource, zoneId))
    );

    loadAndProcessList(
        basePath + FILE_NPCS,
        NpcSourceJson.class,
        npcSource -> npcRepository.save(npcMapper.toEntity(npcSource, zoneId))
    );

    loadAndProcessList(
        basePath + FILE_SETTLEMENTS,
        SettlementSourceJson.class,
        settlementSource -> settlementRepository.save(settlementMapper.toEntity(settlementSource, zoneId))
    );
  }

  private <T> T loadAndProcessSingleSource(String path, Class<T> type, java.util.function.Consumer<T> saver) {
    try (InputStream is = getStream(path)) {
      if (is == null) {
        return null;
      }

      byte[] bytes = is.readAllBytes();
      String hash = DigestUtils.md5DigestAsHex(bytes);
      String dbKey = extractDbKey(path);
      T object = objectMapper.readValue(bytes, type);

      if (isVersionChanged(dbKey, hash)) {
        log.info("[UPDATE] : {}", dbKey);
        saver.accept(object);
        updateVersion(dbKey, hash);
      }
      return object;
    } catch (Exception e) {
      log.error("Error processing file {}", path, e);
      throw new RuntimeException("Failed to load " + path, e);
    }
  }

  private <T> void loadAndProcessList(String path, Class<T> itemType, java.util.function.Consumer<T> saver) {
    try (InputStream is = getStream(path)) {
      if (is == null) {
        return;
      }

      byte[] bytes = is.readAllBytes();
      String hash = DigestUtils.md5DigestAsHex(bytes);
      String dbKey = extractDbKey(path);

      if (isVersionChanged(dbKey, hash)) {
        log.info("[UPDATE] : {}", dbKey);

        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, itemType);
        List<T> items = objectMapper.readValue(bytes, listType);

        items.forEach(saver);
        updateVersion(dbKey, hash);
      }
    } catch (Exception e) {
      log.error("Error processing list file {}", path, e);
      throw new RuntimeException("Failed to load list " + path, e);
    }
  }


  private boolean isVersionChanged(String zoneName, String newHash) {
    Optional<ContentVersionEntity> existing = versionRepository.findById(zoneName);
    return existing.isEmpty() || !existing.get().getContentHash().equals(newHash);
  }

  private void updateVersion(String fileName, String hash) {
    ContentVersionEntity version = ContentVersionEntity.builder()
        .fileName(fileName)
        .contentHash(hash)
        .updatedAt(LocalDateTime.now())
        .build();
    versionRepository.save(version);
  }

  private <T> T loadResource(String path, Class<T> type) throws IOException {
    try (InputStream is = getStream(path)) {
      if (is == null) {
        throw new IOException("Resource not found: " + path);
      }
      return objectMapper.readValue(is, type);
    }
  }

  private InputStream getStream(String path) throws IOException {
    ClassPathResource resource = new ClassPathResource(path);
    return resource.exists() ? resource.getInputStream() : null;
  }

  private String extractDbKey(String fullPath) {
    if (fullPath.startsWith(PATH_ZONE)) {
      return fullPath.substring(PATH_ZONE.length());
    }

    if (fullPath.startsWith(PATH_RESOURCE_ROOT)) {
      return fullPath.substring(PATH_RESOURCE_ROOT.length());
    }

    return fullPath;
  }
}
