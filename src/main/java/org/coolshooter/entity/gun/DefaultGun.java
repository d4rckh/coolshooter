package org.coolshooter.entity.gun;

import java.awt.Color;
import org.coolshooter.entity.player.PlayerEntity;

public class DefaultGun extends Gun {
    public DefaultGun(PlayerEntity owner) {
        super(owner,
                0.2,          // cooldown
                1500,          // speed
                0.8,           // knockback strength
                500,           // max distance
                Color.YELLOW); // bullet color
    }
}
