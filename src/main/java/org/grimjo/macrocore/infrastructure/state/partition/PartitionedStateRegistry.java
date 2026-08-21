package org.grimjo.macrocore.infrastructure.state.partition;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement;
import org.grimjo.macrocore.game.domain.settlement.SmallSettlement.SettlementId;
import org.grimjo.macrocore.game.domain.world.Room;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.game.domain.world.Zone;
import org.grimjo.macrocore.game.domain.world.Zone.ZoneId;
import org.grimjo.macrocore.game.domain.global.WorldState;
import org.springframework.stereotype.Component;

/**
 * Реестр партиционированного состояния.
 * Хранит иммутабельные сущности (Room, Settlement, Zone) в изолированных AtomicReference,
 * что позволяет обновлять их конкурентно без использования глобальных блокировок.
 */
@Component
public class PartitionedStateRegistry {

    private final Map<RoomId, AtomicReference<Room>> rooms = new ConcurrentHashMap<>();
    private final Map<SettlementId, AtomicReference<SmallSettlement>> settlements = new ConcurrentHashMap<>();
    private final Map<NpcId, AtomicReference<NpcBase>> npcs = new ConcurrentHashMap<>();
    private final Map<PlayerId, AtomicReference<Player>> players = new ConcurrentHashMap<>();

    /**
     * Инициализация реестра из глобального состояния (например, после загрузки из БД).
     */
    public void initializeFromWorldState(WorldState worldState) {
        rooms.clear();
        settlements.clear();
        npcs.clear();

        worldState.getRooms().values().forEach(this::putRoom);
        worldState.getSettlements().values().forEach(this::putSettlement);
        worldState.getPopulation().values().forEach(this::putNpc);
        worldState.getPlayers().values().forEach(this::putPlayer);
    }

    // --- Rooms ---

    public void putRoom(Room room) {
        rooms.put(room.getId(), new AtomicReference<>(room));
    }

    public Room getRoom(RoomId id) {
        AtomicReference<Room> ref = rooms.get(id);
        return ref != null ? ref.get() : null;
    }

    public Room updateRoom(RoomId id, UnaryOperator<Room> updater) {
        AtomicReference<Room> ref = rooms.get(id);
        if (ref == null) throw new IllegalArgumentException("Room not found: " + id);
        return ref.updateAndGet(updater);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values().stream().map(AtomicReference::get).collect(Collectors.toList());
    }

    // --- Settlements ---

    public void putSettlement(SmallSettlement settlement) {
        settlements.put(settlement.getId(), new AtomicReference<>(settlement));
    }

    public SmallSettlement getSettlement(SettlementId id) {
        AtomicReference<SmallSettlement> ref = settlements.get(id);
        return ref != null ? ref.get() : null;
    }

    public SmallSettlement updateSettlement(SettlementId id, UnaryOperator<SmallSettlement> updater) {
        AtomicReference<SmallSettlement> ref = settlements.get(id);
        if (ref == null) throw new IllegalArgumentException("Settlement not found: " + id);
        return ref.updateAndGet(updater);
    }

    public Collection<SmallSettlement> getAllSettlements() {
        return settlements.values().stream().map(AtomicReference::get).collect(Collectors.toList());
    }

    // --- NPCs ---

    public void putNpc(NpcBase npc) {
        npcs.put(npc.getId(), new AtomicReference<>(npc));
    }

    public NpcBase getNpc(NpcId id) {
        AtomicReference<NpcBase> ref = npcs.get(id);
        return ref != null ? ref.get() : null;
    }

    public NpcBase updateNpc(NpcId id, UnaryOperator<NpcBase> updater) {
        AtomicReference<NpcBase> ref = npcs.get(id);
        if (ref == null) throw new IllegalArgumentException("Npc not found: " + id);
        return ref.updateAndGet(updater);
    }

    public Collection<NpcBase> getAllNpcs() {
        return npcs.values().stream().map(AtomicReference::get).collect(Collectors.toList());
    }

    // --- Players ---

    public void putPlayer(Player player) {
        players.put(player.getId(), new AtomicReference<>(player));
    }

    public Player getPlayer(PlayerId id) {
        AtomicReference<Player> ref = players.get(id);
        return ref != null ? ref.get() : null;
    }

    public Player updatePlayer(PlayerId id, UnaryOperator<Player> updater) {
        AtomicReference<Player> ref = players.get(id);
        if (ref == null) throw new IllegalArgumentException("Player not found: " + id);
        return ref.updateAndGet(updater);
    }

    public Collection<Player> getAllPlayers() {
        return players.values().stream().map(AtomicReference::get).collect(Collectors.toList());
    }
}
