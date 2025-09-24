package org.coolshooter.entity.gun;

import java.awt.Color;
import org.coolshooter.entity.player.PlayerEntity;

public class HeavyGun extends Gun {
    public HeavyGun(PlayerEntity owner) {
        super(owner,
                0.5,           // cooldown: slower fire rate
                1200,          // speed: bullets slower
                0.8,           // knockback: strong
                450,           // max distance: longer range
                Color.ORANGE); // bullet color
    }
}
