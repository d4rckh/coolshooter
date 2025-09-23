package org.coolshooter.entity.effect;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.common.RenderableEntity;

public class RenderableEffectEntity extends RenderableEntity {
    private final double lifetime; // how long effect lasts (seconds)
    private double elapsed = 0; // time passed

    private final RenderableCollidableEntity followEntity;
    private final Position offset;

    @Getter
    @Setter
    private Color baseColor = Color.ORANGE;

    public RenderableEffectEntity(Game game, Position pos, double lifetime, Color color) {
        this(game, pos, lifetime, color, null);
    }

    // This makes sure the effect stays fixed to the effect
    // It is used for hitting indicator
    public RenderableEffectEntity(Game game, Position offset, double lifetime, Color color,
            RenderableCollidableEntity parent) {
        super(game, offset.getX(), offset.getY(), true);
        this.lifetime = lifetime;
        this.baseColor = color;
        this.offset = offset;
        this.followEntity = parent;
    }

    @Override
    public void init() {
    }

    @Override
    public void update(double delta) {
        elapsed += delta;
        if (elapsed >= lifetime) {
            destroy();
            return;
        }

        if (followEntity != null && offset != null) {
            // Follow parent's center
            this.getPosition().setX(followEntity.getPosition().getX() + this.offset.getX());
            this.getPosition().setY(followEntity.getPosition().getY() + this.offset.getY());
        }
    }

    @Override
    public void renderShape(Graphics g) {
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY();

        double ratio = elapsed / lifetime; // 0 → 1 over lifetime

        // Circle grows from 0 to max size
        int maxSize = 60; // pixels
        int size = (int) (maxSize * ratio * getGame().getCamera().getZoom());

        // Fade alpha from 255 → 0
        int alpha = (int) (255 * (1 - ratio));
        alpha = Math.max(0, Math.min(255, alpha));

        // Apply fading color
        Color fadingColor = new Color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                alpha);

        g.setColor(fadingColor);
        g.fillOval(x - size / 2, y - size / 2, size, size);
    }
}
