package org.coolshooter.entity.player;

import java.awt.*;
import java.util.Random;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.gun.WeakGun;

public class NPCEntity extends PlayerEntity {
    private final RenderableCollidableEntity entityToFollow;
    private final Random random = new Random();
    private double shootCooldown = 0;

    public NPCEntity(Game game, Position position, RenderableCollidableEntity entityToFollow) {
        super(game, position.getX(), position.getY());
        this.entityToFollow = entityToFollow;
        setColor(Color.BLUE);
        setWidth(50);
        setHeight(50);
        setHp(50);
        setMaxHp(50);
        setShape(ShapeType.RECTANGLE);
        setSpeed(400);
    }

    
    @Override
    public void init() {
        super.init();
        this.gun = new WeakGun(this);
    }

    @Override
    public void update(double delta) {
        if (entityToFollow != null && !entityToFollow.isDestroyed()) {
            double dx = entityToFollow.getPosition().getX() - getPosition().getX();
            double dy = entityToFollow.getPosition().getY() - getPosition().getY();

            double length = Math.sqrt(dx * dx + dy * dy);

            double vx = dx / length;
            double vy = dy / length;

            if (length > 100) {
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
                shootCooldown = 0.5 + random.nextDouble() * 1.5;
            }

            // getPosition().setX(x -> x + velX * speed * delta);
            // getPosition().setY(y -> y + velY * speed * delta);
        } else {
            velX = 0;
            velY = 0;
        }

        super.update(delta);
    }
}
