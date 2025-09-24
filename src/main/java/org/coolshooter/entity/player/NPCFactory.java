package org.coolshooter.entity.player;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.gun.HeavyGun;
import org.coolshooter.entity.gun.RapidGun;
import org.coolshooter.entity.gun.WeakGun;

public class NPCFactory {

    private static final Random random = new Random();

    public static NPCEntity createNPC(NPCType type, Game game, Position pos, RenderableEntity target) {
        NPCEntity.NPCConfig cfg = new NPCEntity.NPCConfig();

        switch (type) {
            case FAST_CHASER -> {
                cfg.hp = 40;
                cfg.maxHp = 40;
                cfg.speed = 500;
                cfg.shootCooldown = 1.0;
                cfg.stopDistance = 150;
                cfg.gun = RapidGun.class;
                cfg.spriteName = "fast_chaser";
            }
            case TANK -> {
                cfg.hp = 100;
                cfg.maxHp = 100;
                cfg.speed = 300;
                cfg.shootCooldown = 2.0;
                cfg.stopDistance = 100;
                cfg.gun = HeavyGun.class;
                cfg.spriteName = "tank";
            }
            case GLASS_CANNON -> {
                cfg.hp = 20;
                cfg.maxHp = 20;
                cfg.speed = 400;
                cfg.shootCooldown = 1.5;
                cfg.stopDistance = 250;
                cfg.gun = WeakGun.class;
            }
        }

        return new NPCEntity(game, pos, target, cfg);
    }

    /**
     * Creates a random NPC based on probabilities.
     * Example probabilities: FAST_CHASER=50%, TANK=30%, GLASS_CANNON=20%
     */
    public static NPCEntity createRandomNPC(Game game, Position pos, RenderableEntity target) {
        NavigableMap<Double, NPCType> map = new TreeMap<>();
        double total = 0;

        // define probabilities (sum should be 1.0)
        total += 0.5; map.put(total, NPCType.FAST_CHASER);
        total += 0.3; map.put(total, NPCType.TANK);
        total += 0.2; map.put(total, NPCType.GLASS_CANNON);

        double r = random.nextDouble();
        NPCType selectedType = map.higherEntry(r).getValue();

        return createNPC(selectedType, game, pos, target);
    }
}
