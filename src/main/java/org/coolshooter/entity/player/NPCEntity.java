package org.coolshooter.entity.player;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.gun.DefaultGun;
import org.coolshooter.entity.gun.Gun;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NPCEntity extends PlayerEntity {
    private final RenderableEntity entityToFollow;
    private double shootCooldown;
    private final double defaultShootCooldown;
    private final double stopDistance;
    private final Class<? extends Gun> customGun;

    public static class NPCConfig {
        public String spriteName = "enemy1";
        public Color color = Color.RED;
        public int width = 90;
        public int height = 90;
        public int hp = 50;
        public int maxHp = 50;
        public int speed = 500;
        public double shootCooldown = 1.0; // seconds
        public double stopDistance = 200; // distance to stop chasing
        public Class<? extends Gun> gun = null; // fallback: WeakGun
    }

    public NPCEntity(Game game, Position position, RenderableEntity entityToFollow, NPCConfig config) {
        super(game, position.getX(), position.getY(), config.spriteName);
        this.entityToFollow = entityToFollow;

        setColor(config.color);
        setWidth(config.width);
        setHeight(config.height);
        setHp(config.hp);
        setMaxHp(config.maxHp);
        setSpeed(config.speed);

        this.shootCooldown = config.shootCooldown;
        this.defaultShootCooldown = config.shootCooldown;
        this.stopDistance = config.stopDistance;
        this.customGun = config.gun;
    }

    @Override
    public void init() {
        super.init();
        if (this.customGun != null) {
            try {
                this.gun = this.customGun.getConstructor(PlayerEntity.class).newInstance(this);
            } catch (Exception e) {
                e.printStackTrace();
                this.gun = new DefaultGun(this);
            }
        } else {
            this.gun = new DefaultGun(this);
        }
    }

    @Override
    public void update(double delta) {
        if (entityToFollow != null && !entityToFollow.isDestroyed()) {
            double dx = entityToFollow.getPosition().getX() + (entityToFollow.getWidth() / 2.0) - getPosition().getX()
                    - this.getHeight() / 2.0;
            double dy = entityToFollow.getPosition().getY() + (entityToFollow.getHeight() / 2.0) - getPosition().getY()
                    - this.getWidth() / 2.0;

            double length = Math.sqrt(dx * dx + dy * dy);

            double vx = dx / length;
            double vy = dy / length;

            if (length > stopDistance) {
                velX = vx;
                velY = vy;
            } else {
                velX = 0;
                velY = 0;
            }

            // Shooting logic
            shootCooldown -= delta;
            if (shootCooldown <= 0 && gun != null) {
                gun.shoot(vx, vy);
                shootCooldown = defaultShootCooldown;
            }

        } else {
            velX = 0;
            velY = 0;
        }

        super.update(delta);
    }
}
