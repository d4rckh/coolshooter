package org.coolshooter.entity;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.player.NPCEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EntitySpawner {
    private final Game game;

    public void spawnNPC() {
        if (game.getUserPlayerEntity() == null)
            return;

        double playerX = game.getUserPlayerEntity().getPosition().getX();
        double playerY = game.getUserPlayerEntity().getPosition().getY();

        double offsetX = (Math.random() - 0.5) * 1000;
        double offsetY = (Math.random() - 0.5) * 1000;

        NPCEntity npc = new NPCEntity(game, new Position(playerX + offsetX, playerY + offsetY), game.getUserPlayerEntity());
        game.getEntityManager().addEntity(npc);

        log.info("Spawning NPC at ({}, {})", npc.getPosition().getX(), npc.getPosition().getY());
    }

}
