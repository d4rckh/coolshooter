package org.coolshooter.entity.gun;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import org.coolshooter.entity.Bullet;
import org.coolshooter.entity.player.PlayerEntity;

public abstract class Gun {
    @Getter
    protected final PlayerEntity owner;
    protected final double cooldown; // seconds
    protected double timeSinceLastShot = 0;

    @Getter
    protected final int speed; // pixels/sec

    @Getter
    protected final double knockbackStrength;

    @Getter
    protected final double maxDistance; // pixels

    @Setter
    protected Color bulletColor;

    public Gun(PlayerEntity owner, double cooldown, int speed, double knockbackStrength, double maxDistance, Color bulletColor) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.speed = speed;
        this.knockbackStrength = knockbackStrength;
        this.maxDistance = maxDistance;
        this.bulletColor = bulletColor;
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

            // Include player velocity
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
