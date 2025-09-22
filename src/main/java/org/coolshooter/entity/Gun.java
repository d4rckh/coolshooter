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
    private final int speed = 2000; // pixels/sec

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

    public void shoot(double dirX, double dirY, double extraVelX, double extraVelY) {
        if (timeSinceLastShot >= cooldown) {
            // Create a bullet at the owner's position
            Bullet bullet = new Bullet(
                    this.game,
                    bulletColor,
                    this,
                    owner.getPosition(),
                    dirX, // base direction X
                    dirY, // base direction Y
                    extraVelX, // extra velocity X (e.g., player movement)
                    extraVelY // extra velocity Y
            );

            // Add bullet to the game
            owner.getGame().addEntity(bullet);

            // Reset shooting cooldown
            timeSinceLastShot = 0;
        }
    }
}
