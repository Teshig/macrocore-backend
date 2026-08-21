package org.grimjo.macrocore.game.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grimjo.macrocore.game.domain.actor.Player;
import org.grimjo.macrocore.game.domain.actor.Player.PlayerId;
import org.grimjo.macrocore.game.domain.actor.NpcBase;
import org.grimjo.macrocore.game.domain.actor.NpcBase.NpcId;
import org.grimjo.macrocore.game.domain.actor.NpcStatus;
import org.grimjo.macrocore.game.domain.world.Room.RoomId;
import org.grimjo.macrocore.infrastructure.bus.CommandHandler;
import org.grimjo.macrocore.infrastructure.state.partition.PartitionedStateRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttackCommandHandler implements CommandHandler<AttackCommand> {

    private final PartitionedStateRegistry registry;
    private static final int BASE_DAMAGE = 15;

    @Override
    public void handle(AttackCommand command) {
        RoomId attackerRoomId = getActorRoomId(command.getAttackerId());
        if (attackerRoomId == null) {
            return;
        }

        RoomId targetRoomId = getActorRoomId(command.getTargetId());
        if (targetRoomId == null) {
            return;
        }

        if (!attackerRoomId.equals(targetRoomId)) {
            log.debug("AttackCommand failed: attacker and target are in different rooms");
            return;
        }

        applyDamage(command.getTargetId(), BASE_DAMAGE);
        log.info("Actor {} attacked {}. Target health reduced.", command.getAttackerId(), command.getTargetId());
    }

    private RoomId getActorRoomId(Long id) {
        Player player = registry.getPlayer(PlayerId.of(id));
        if (player != null && !player.isDead()) {
            return player.getRoomId();
        }
        NpcBase npc = registry.getNpc(NpcId.of(id));
        if (npc != null && !npc.isDead()) {
            return npc.getRoomId();
        }
        return null;
    }

    private void applyDamage(Long id, int damage) {
        Player player = registry.getPlayer(PlayerId.of(id));
        if (player != null) {
            registry.updatePlayer(PlayerId.of(id), current -> {
                int newHealth = Math.max(0, current.getHealth() - damage);
                NpcStatus newStatus = newHealth == 0 ? NpcStatus.DEAD : current.getStatus();
                return current.toBuilder()
                       .health(newHealth)
                       .status(newStatus)
                       .build();
            });
            return;
        }

        NpcBase npc = registry.getNpc(NpcId.of(id));
        if (npc != null) {
            registry.updateNpc(NpcId.of(id), current -> {
                int newHealth = Math.max(0, current.getHealth() - damage);
                NpcStatus newStatus = newHealth == 0 ? NpcStatus.DEAD : current.getStatus();
                return current.toBuilder()
                       .health(newHealth)
                       .status(newStatus)
                       .build();
            });
        }
    }

    @Override
    public Class<AttackCommand> getCommandType() {
        return AttackCommand.class;
    }
}
