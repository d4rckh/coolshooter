package org.coolshooter.entity.common;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.coolshooter.Game;
import org.coolshooter.entity.trait.Collidable;

public abstract class RenderableCollidableEntity extends RenderableEntity implements Collidable {
    @Getter
    @Setter
    protected ShapeType shape = ShapeType.RECTANGLE;

    public RenderableCollidableEntity(Game game, double worldX, double worldY) {
        super(game, worldX, worldY, true);
    }

    /**
     * Returns a shape for collision calculations (world coordinates)
     */
    public Shape getCollisionShape() {
        double x = position.getX();
        double y = position.getY();
        double w = width;
        double h = height;

        return switch (shape) {
            case RECTANGLE -> new Rectangle2D.Double(x, y, w, h);
            case OVAL -> new Ellipse2D.Double(x, y, w, h);
        };
    }

    public boolean collidesWith(RenderableCollidableEntity other) {
        return this.getCollisionShape().getBounds2D()
                .intersects(other.getCollisionShape().getBounds2D());
    }

    public Point getIntersectionPoint(RenderableCollidableEntity other) {
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
        int x = (int) screenPosition.getX();
        int y = (int) screenPosition.getY();
        int w = (int) (width * getGame().getCamera().getZoom());
        int h = (int) (height * getGame().getCamera().getZoom());

        switch (shape) {
            case RECTANGLE -> g.fillRect(x, y, w, h);
            case OVAL -> g.fillOval(x, y, w, h);
        }

        // --- DEBUG: draw collision shape ---
        // Graphics2D g2 = (Graphics2D) g.create();
        // g2.setColor(new Color(255, 0, 0, 100)); // semi-transparent red
        // Shape collisionShape = getCollisionShape();
        // g2.fill(collisionShape); // draw filled shape for visibility
        // g2.setColor(Color.RED);
        // g2.draw(collisionShape); // draw outline
        // g2.dispose();
    }

    public void onCollision(RenderableCollidableEntity entity) {
    }
}
