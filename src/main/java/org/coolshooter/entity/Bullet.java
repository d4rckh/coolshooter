package org.coolshooter.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.BasicShapeCollidableEntity;
import org.coolshooter.entity.effect.RenderableEffectEntity;
import org.coolshooter.entity.gun.Gun;
import org.coolshooter.entity.player.NPCEntity;
import org.coolshooter.entity.player.PlayerEntity;
import org.coolshooter.entity.trait.Collidable;

@Slf4j
public class Bullet extends BasicShapeCollidableEntity {
    private final double velX;
    private final double velY;
    private final double extraVelX;
    private final double extraVelY;
    @Getter
    private final Gun originGun;
    private double travelledDistance = 0;
    @Getter
    private final int damage = 10;

    private static int width = 10;
    private static int height = 10;
    

    public Bullet(Game game, Color color, Gun gun, Position pos,
            double velX, double velY, double extraVelX, double extraVelY) {
        super(game,
                // Start at player center, then offset by half bullet size to align centers
                gun.getOwner().getCenter().getX() - Bullet.width / 2 + velX * 5,
                gun.getOwner().getCenter().getY() - Bullet.height / 2 + velY * 5);

        this.originGun = gun;
        this.velX = velX * gun.getSpeed();
        this.velY = velY * gun.getSpeed();
        this.extraVelX = extraVelX;
        this.extraVelY = extraVelY;

        setColor(color);
        setWidth(Bullet.width); // bullet width
        setHeight(Bullet.height); // bullet height
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
    public void onCollision(Collidable entity) {
        // Ignore the owner
        if (entity == originGun.getOwner())
            return;

        // Prevent friendly fire for NPCs
        if (entity instanceof NPCEntity && originGun.getOwner() instanceof NPCEntity)
            return;

        // Handle player hit
        if (entity instanceof PlayerEntity player) {

            Point intersectionPoint = entity.getIntersectionPoint(this);

            if (intersectionPoint == null)
                return;

            destroy();

            player.takeDamage(this.damage);
            player.knockback(velX, velY, originGun.getKnockbackStrength());

            // Add visual hit effect
            getGame().addEntity(new RenderableEffectEntity(
                    getGame(),
                    // Offset stored as position
                    // TODO: store as something else
                    new Position(
                            intersectionPoint.getX() - player.getPosition().getX(),
                            intersectionPoint.getY() - player.getPosition().getY()),
                    0.3,
                    this.getColor(),
                    player));

        }
    }
}
