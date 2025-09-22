package org.coolshooter.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.effect.RenderableEffectEntity;
import org.coolshooter.entity.player.NPCEntity;
import org.coolshooter.entity.player.PlayerEntity;

@Slf4j
public class Bullet extends RenderableCollidableEntity {
    private final double velX;
    private final double velY;
    private final double extraVelX;
    private final double extraVelY;
    @Getter
    private final Gun originGun;
    private double travelledDistance = 0;
    @Getter
    private final int damage = 10;

    public Bullet(Game game, Color color, Gun gun, Position pos,
                  double velX, double velY, double extraVelX, double extraVelY) {
        super(game, pos.getX() + 25 + velX * 25, pos.getY() + 25 + velY * 25);

        this.originGun = gun;
        this.velX = velX * gun.getSpeed();
        this.velY = velY * gun.getSpeed();
        this.extraVelX = extraVelX;
        this.extraVelY = extraVelY;

        setColor(color);
        setWidth(10);
        setHeight(10);
        setShape(ShapeType.OVAL);
    }

    @Override
    public void init() {
        // Nothing needed
    }

    @Override
    public void update(double delta) {
        double dx = velX + extraVelX;
        double dy = velY + extraVelY;

        getPosition().setX(x -> x + dx * delta);
        getPosition().setY(y -> y + dy * delta);

        travelledDistance += Math.sqrt(dx * dx + dy * dy) * delta;

        // Destroy if max distance reached
        if (travelledDistance >= originGun.getMaxDistance()) {
            destroy();
        }
    }

    @Override
    public void onCollision(RenderableCollidableEntity entity) {
        // Ignore the owner
        if (entity == originGun.getOwner()) return;

        // Prevent friendly fire for NPCs
        if (entity instanceof NPCEntity && originGun.getOwner() instanceof NPCEntity) return;

        // Handle player hit
        if (entity instanceof PlayerEntity player) {
            log.info("hit");

            destroy();
            
            player.takeDamage(this.damage);
            player.knockback(velX, velY, originGun.getKnockbackStrength());

            // Add visual hit effect
            getGame().addEntity(new RenderableEffectEntity(
                    getGame(),
                    new Position(getPosition().getX(), getPosition().getY()),
                    0.3,
                    this.getColor()
            ));

        }
    }
}
