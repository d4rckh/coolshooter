package org.coolshooter.entity.gun;

import java.awt.Color;
import org.coolshooter.entity.player.PlayerEntity;

public class RapidGun extends Gun {
    public RapidGun(PlayerEntity owner) {
        super(owner,
                0.1,           // cooldown: shoots faster
                1800,          // speed: bullets move faster
                0.3,           // knockback: weaker
                400,           // max distance: shorter range
                Color.CYAN);   // bullet color
    }
}
