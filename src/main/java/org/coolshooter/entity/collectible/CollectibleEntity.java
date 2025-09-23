package org.coolshooter.entity.collectible;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.coolshooter.Game;
import org.coolshooter.entity.common.BasicShapeCollidableEntity;
import org.coolshooter.entity.player.UserPlayerEntity;
import org.coolshooter.entity.trait.Collectible;
import org.coolshooter.entity.trait.Collidable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CollectibleEntity extends BasicShapeCollidableEntity implements Collectible {
    private final String displayName;
    private double animationTime;

    private double anchorX;
    private double anchorY;

    public CollectibleEntity(Game game, double worldX, double worldY, String displayName) {
        super(game, worldX, worldY);
        this.displayName = displayName;
        this.animationTime = 0;

        this.anchorX = worldX + width / 2.0;
        this.anchorY = worldY + height / 2.0;
    }

    @Override
    public void init() {
    }

    private int baseSize = 30;
    private int amplitude = 5;

    @Override
    public void update(double delta) {
        this.animationTime += delta;

        // Oscillating size
        double scale = Math.sin(this.animationTime * 2); // -1 to 1
        double newSize = baseSize + scale * amplitude;

        // Position so element stays centered on the original anchor
        int newX = (int) (anchorX - newSize / 2);
        int newY = (int) (anchorY - newSize / 2);

        this.getPosition().setX(newX);
        this.getPosition().setY(newY);

        this.setWidth((int) newSize);
        this.setHeight((int) newSize);
    }

    @Override
    public void onCollision(Collidable entity) {
        if (entity instanceof UserPlayerEntity player) {
            if (player.heal(5))
                this.destroy();
        }
    }

    @Override
    public void renderShape(Graphics g) {
        super.renderShape(g);

        Graphics2D g2 = (Graphics2D) g.create();

        double zoom = this.getGame().getCamera().getZoom();
        g2.setFont(new Font("Arial", Font.BOLD, (int) (30 * zoom)));

        // Get screen coordinates of the anchor
        int screenX = (int) getGame().getCamera().toScreenX(anchorX);
        int screenY = (int) getGame().getCamera().toScreenY(anchorY);

        int textWidth = g2.getFontMetrics().stringWidth(displayName);
        int textX = screenX - textWidth / 2;
        int textY = screenY - (int) (this.getHeight() / 2 * zoom) - 5; // a bit above

        g2.setColor(Color.WHITE);
        g2.drawString(displayName, textX, textY);

        g2.dispose();
    }

}
