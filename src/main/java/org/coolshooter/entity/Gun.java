package org.coolshooter.entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.entity.player.PlayerEntity;

public class Gun {
    @Getter
    private final PlayerEntity owner;
    private final Game game;
    private final double cooldown = 0.01; // seconds
    private double timeSinceLastShot = 0;

    @Getter
    private final int speed = 1000; // pixels/sec

    @Getter
    private final double knockbackStrength = 700;

    @Getter
    private final double maxDistance = 500; // pixels

    @Setter
    private Color bulletColor = Color.YELLOW;

    public Gun(PlayerEntity owner) {
        this.owner = owner;
        this.game = owner.getGame();
    }

    public void update(double delta) {
        if (timeSinceLastShot < cooldown) {
            timeSinceLastShot += delta;
        }
    }

    public void shoot(double dx, double dy) {
        if (timeSinceLastShot >= cooldown) {
            // create a bullet at the player's world position
            Bullet bullet = new Bullet(this.game, bulletColor, this, owner.getPosition(), dx, dy);
            owner.getGame().addEntity(bullet);
            timeSinceLastShot = 0;
        }
    }
}
