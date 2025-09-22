package org.coolshooter.entity.gun;

import java.awt.Color;
import org.coolshooter.entity.player.PlayerEntity;

public class WeakGun extends Gun {
    public WeakGun(PlayerEntity owner) {
        super(owner,
                0.5, // slower cooldown (shoots less often)
                800, // slower bullet speed
                0.2, // weaker knockback
                400, // shorter max distance
                Color.ORANGE); // different color for NPC bullets
    }
}
