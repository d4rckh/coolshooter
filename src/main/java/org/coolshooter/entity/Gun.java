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
    private final double knockbackStrength = 0.5;

    @Getter
    private final double maxDistance = 300; // pixels

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

    public void shoot(double dirX, double dirY) {
        if (timeSinceLastShot >= cooldown) {
            // Normalize direction
            double length = Math.sqrt(dirX * dirX + dirY * dirY);
            if (length != 0) {
                dirX /= length;
                dirY /= length;
            }

            // Take current player velocity
            double extraVelX = owner.getVelX() * owner.getSpeed();
            double extraVelY = owner.getVelY() * owner.getSpeed();

            Bullet bullet = new Bullet(
                    owner.getGame(),
                    bulletColor,
                    this,
                    owner.getPosition(),
                    dirX, dirY,
                    extraVelX, extraVelY);

            owner.getGame().getEntityManager().addEntity(bullet);

            timeSinceLastShot = 0;
        }
    }

}
