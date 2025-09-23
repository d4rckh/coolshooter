package org.coolshooter.entity.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.coolshooter.Game;
import org.coolshooter.entity.trait.Collidable;

@Slf4j
public abstract class BasicShapeCollidableEntity extends RenderableEntity implements Collidable {
    @Getter
    @Setter
    protected ShapeType shape = ShapeType.RECTANGLE;

    public BasicShapeCollidableEntity(Game game, double worldX, double worldY) {
        super(game, worldX, worldY, true);
    }

    /**
     * Returns a shape for collision calculations (world coordinates)
     */
    public Shape getCollisionShape() {
        double w = width * scale;
        double h = height * scale;

        // recenter collision shape
        double x = position.getX() + (width - w) / 2;
        double y = position.getY() + (height - h) / 2;

        return switch (shape) {
            case RECTANGLE -> new Rectangle2D.Double(x, y, w, h);
            case OVAL -> new Ellipse2D.Double(x, y, w, h);
        };
    }

    @Override
    public boolean collidesWith(Collidable other) {
        return this.getCollisionShape().getBounds2D()
                .intersects(other.getCollisionShape().getBounds2D());
    }

    @Override
    public Point getIntersectionPoint(Collidable other) {
        Shape thisShape = this.getCollisionShape();
        Shape otherShape = other.getCollisionShape();

        Area area1 = new Area(thisShape);
        area1.intersect(new Area(otherShape)); // intersection area

        if (!area1.isEmpty()) {
            // Get bounds of intersection area
            Rectangle2D bounds = area1.getBounds2D();
            // Return center of the intersection as approximate collision point
            int x = (int) (bounds.getX() + bounds.getWidth() / 2);
            int y = (int) (bounds.getY() + bounds.getHeight() / 2);
            return new Point(x, y);
        }
        return null; // no intersection
    }

    /**
     * Render using shape-specific graphics
     */
    @Override
    public void renderShape(Graphics g) {
        g.setColor(color);

        int baseW = (int) (width * getGame().getCamera().getZoom());
        int baseH = (int) (height * getGame().getCamera().getZoom());

        int scaledW = (int) (baseW * scale);
        int scaledH = (int) (baseH * scale);

        // recenter on screen
        int x = (int) (screenPosition.getX() + baseW / 2 - scaledW / 2);
        int y = (int) (screenPosition.getY() + baseH / 2 - scaledH / 2);

        switch (shape) {
            case RECTANGLE -> g.fillRect(x, y, scaledW, scaledH);
            case OVAL -> g.fillOval(x, y, scaledW, scaledH);
        }
    }

    public void onCollision(BasicShapeCollidableEntity entity) {
    }
}
