package org.coolshooter.entity.collectible;

import java.awt.Color;

import org.coolshooter.Game;
import org.coolshooter.entity.player.UserPlayerEntity;
import org.coolshooter.entity.trait.Collidable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HealthCollectibleEntity extends CollectibleEntity {

    public HealthCollectibleEntity(Game game, double worldX, double worldY) {
        super(game, worldX, worldY, "+5 HP");
        this.setHeight(20);
        this.setWidth(20);
        this.setColor(Color.RED);
        this.setShape(ShapeType.RECTANGLE);
    }

    @Override
    public void onCollision(Collidable entity) {
        if (entity instanceof UserPlayerEntity player) {
            if (player.heal(5))
                this.destroy();
        }
    }

}
