package org.coolshooter.entity.player;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.gun.WeakGun;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NPCEntity extends PlayerEntity {
    private final RenderableEntity entityToFollow;
    private double shootCooldown = 1;

    public NPCEntity(Game game, Position position, RenderableEntity entityToFollow) {
        super(game, position.getX(), position.getY());
        this.entityToFollow = entityToFollow;
        setColor(Color.RED);
        setWidth(90);
        setHeight(90);
        setHp(50);
        setMaxHp(50);
        setSpeed(500);
    }


    @Override
    public void init() {
        super.init();
        this.gun = new WeakGun(this);
    }

    @Override
    public void update(double delta) {
        if (entityToFollow != null && !entityToFollow.isDestroyed()) {
            double dx = entityToFollow.getPosition().getX() + (entityToFollow.getWidth() / 2) - getPosition().getX() - this.getHeight() / 2;
            double dy = entityToFollow.getPosition().getY() + (entityToFollow.getHeight() / 2) - getPosition().getY() - this.getWidth() / 2;

            double length = Math.sqrt(dx * dx + dy * dy);

            double vx = dx / length;
            double vy = dy / length;

            if (length > 200) {
                velX = vx;
                velY = vy;
            } else {
                velX = 0;
                velY = 0;
            }
            
            // Shoot randomly at player
            shootCooldown -= delta;
            if (shootCooldown <= 0 && gun != null) {
                gun.shoot(vx, vy);
                shootCooldown = 1;
            }

        } else {
            velX = 0;
            velY = 0;
        }

        super.update(delta);
    }
}
