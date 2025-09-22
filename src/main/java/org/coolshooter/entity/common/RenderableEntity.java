package org.coolshooter.entity.common;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import org.coolshooter.DynamicPosition;
import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.Entity;
import org.coolshooter.entity.trait.Renderable;

public abstract class RenderableEntity extends Entity implements Renderable {
    private final boolean followWorld;
    @Getter
    @Setter
    protected Position position;
    @Getter
    protected Position screenPosition;
    @Getter
    @Setter
    protected Color color = Color.RED;
    @Getter
    @Setter
    protected int width = 50;
    @Getter
    @Setter
    protected int height = 50;

    public RenderableEntity(Game game, DynamicPosition position) {
        super(game);
        this.followWorld = false;
        this.position = position;
        this.screenPosition = position;
    }

    public RenderableEntity(Game game, double x, double y, boolean followWorld) {
        super(game);
        this.followWorld = followWorld;
        this.position = new Position(x, y);
        this.screenPosition = new Position(position.getX(), position.getY());
    }

    /**
     * Update screen position from camera
     */
    public void updateScreenPosition() {
        screenPosition = getGame().getCamera().toScreen(position);
    }

    /**
     * Render rectangle/oval entities
     */
    public void render(Graphics g) {
        if (followWorld) updateScreenPosition(); // keep screen position updated

        renderShape(g);
    }

    public abstract void renderShape(Graphics g);


    public enum ShapeType {RECTANGLE, OVAL}
}
