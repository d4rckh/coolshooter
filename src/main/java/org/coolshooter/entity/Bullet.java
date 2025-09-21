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
    private double velX;
    private double velY;
    @Getter
    private final Gun originGun;
    private double travelledX;
    private double travelledY;
    @Getter
    private final int damage = 10;

    public Bullet(Game game, Color color, Gun gun, Position playerPos, double velX, double velY) {
        super(game,
                playerPos.getX() + 25 + velX * 25,
                playerPos.getY() + 25 + velY * 25);

        this.originGun = gun;
        this.velX = velX;
        this.velY = velY;

        // Use RenderableEntity customization
        setColor(color);
        setHeight(10);
        setWidth(10);
        setShape(ShapeType.OVAL);
    }

    @Override
    public void init() {
        // nothing needed for now
    }

    @Override
    public void update(double delta) {
        // Apply damping to velocity
        double damping = 0.98; // Reduce speed by 2% per frame
        double speedX = velX * originGun.getSpeed();
        double speedY = velY * originGun.getSpeed();

        // Update position with current speed
        getPosition().setX(x -> x + speedX * delta);
        getPosition().setY(y -> y + speedY * delta);

        travelledX += speedX * delta;
        travelledY += speedY * delta;

        // Reduce the velocity for next frame
        velX *= damping;
        velY *= damping;

        // Destroy bullet if travelled far enough
        if (Math.sqrt(travelledX * travelledX + travelledY * travelledY) >= originGun.getMaxDistance() ||
            (Math.abs(velX) <= 0.05 && Math.abs(velY) <= 0.05)) {
            destroy();
        }
    }

    @Override
    public void onCollision(RenderableCollidableEntity entity) {
        // Only collide with players (and not the shooter)

        if (entity == originGun.getOwner())
            return;

        if (entity instanceof NPCEntity && originGun.getOwner() instanceof NPCEntity)
            return;

        if (entity instanceof PlayerEntity player) {
            player.takeDamage(this.damage);
            player.knockback(velX, velY, this.originGun.getKnockbackStrength());
            // Add hit effect
            getGame().addEntity(new RenderableEffectEntity(
                    getGame(),
                    new Position(getPosition().getX(), getPosition().getY()),
                    0.3, // lifetime in seconds
                    this.getColor()));

            this.destroy();
        }
    }

}
