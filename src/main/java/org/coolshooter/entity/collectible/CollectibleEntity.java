package org.coolshooter.entity.collectible;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.coolshooter.Game;
import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.player.UserPlayerEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CollectibleEntity extends RenderableCollidableEntity {
    private final String displayName;
    private double animationTime;

    public CollectibleEntity(Game game, double worldX, double worldY, String displayName) {
        super(game, worldX, worldY);
        this.displayName = displayName;
        this.animationTime = 0;
    }

    @Override
    public void init() {
    }

    @Override
    public void update(double delta) {
        this.animationTime += delta;

        this.setHeight((int) (30 * (1 + Math.abs(Math.sin(this.animationTime * 2)))));
        this.setWidth((int) (30 * (1 + Math.abs(Math.sin(this.animationTime * 2)))));
    }

    @Override
    public void onCollision(RenderableCollidableEntity entity) {
        if (entity instanceof UserPlayerEntity player) {
            if (player.heal(5))
                this.destroy();
        }
    }

    @Override
    public void renderShape(Graphics g) {
        super.renderShape(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setFont(new Font("Arial", Font.BOLD, (int) (30 * this.getGame().getCamera().getZoom())));

        // Center the text above the entity
        int textWidth = g2.getFontMetrics().stringWidth(displayName);
        int textX = (int) (getScreenPosition().getX() + getWidth() / 2.0 - textWidth / 2);
        int textY = (int) (getScreenPosition().getY() - 2);

        g2.setColor(Color.WHITE);
        g2.drawString(displayName, textX, textY);

        g2.dispose();
    }

}
