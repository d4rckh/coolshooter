package org.coolshooter.entity.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import org.coolshooter.Game;
import org.coolshooter.ResourceManager.Sprite;
import org.coolshooter.entity.trait.Collidable;

@Slf4j
public class SpriteCollidableEntity extends RenderableEntity implements Collidable {

    @Getter
    private final Sprite sprite;

    @Setter
    private int currentFrame = 0;

    @Setter
    private boolean flipHorizontally = false;

    @Setter
    private long frameDurationMs = 200; // time per frame
    private long lastFrameSwitch = System.currentTimeMillis();

    public SpriteCollidableEntity(Game game, double worldX, double worldY, String spriteName) {
        super(game, worldX, worldY, true);
        this.sprite = game.getResourceManager().getSprite(spriteName);

        // set default width/height to sprite dimensions
        this.width = sprite.getFrame(0).getWidth();
        this.height = sprite.getFrame(0).getHeight();
    }

    public Shape getCollisionShape() {
        double w = width * scale;
        double h = height * scale;

        double x = position.getX() + (width - w) / 2;
        double y = position.getY() + (height - h) / 2;

        return new Rectangle2D.Double(x, y, w, h);
    }

    @Override
    public boolean collidesWith(Collidable other) {
        return this.getCollisionShape().getBounds2D()
                .intersects(other.getCollisionShape().getBounds2D());
    }

    @Override
    public Point getIntersectionPoint(Collidable other) {
        Rectangle2D bounds1 = this.getCollisionShape().getBounds2D();
        Rectangle2D bounds2 = other.getCollisionShape().getBounds2D();

        Rectangle2D intersection = bounds1.createIntersection(bounds2);
        if (!intersection.isEmpty()) {
            int x = (int) (intersection.getX() + intersection.getWidth() / 2);
            int y = (int) (intersection.getY() + intersection.getHeight() / 2);
            return new Point(x, y);
        }
        return null;
    }

    @Override
    public void renderShape(Graphics g) {
        // update animation frame
        long now = System.currentTimeMillis();
        if (now - lastFrameSwitch >= frameDurationMs) {
            currentFrame = (currentFrame + 1) % sprite.getFrameCount();
            lastFrameSwitch = now;
        }

        Image frame = sprite.getFrame(currentFrame);

        int baseW = (int) (width * getGame().getCamera().getZoom());
        int baseH = (int) (height * getGame().getCamera().getZoom());

        int scaledW = (int) (baseW * scale);
        int scaledH = (int) (baseH * scale);

        int x = (int) (screenPosition.getX() + baseW / 2 - scaledW / 2);
        int y = (int) (screenPosition.getY() + baseH / 2 - scaledH / 2);

        Graphics2D g2d = (Graphics2D) g.create();

        if (flipHorizontally) {
            // draw flipped horizontally by inverting width
            g2d.drawImage(frame,
                    x + scaledW, y, // destination top-left
                    x, y + scaledH, // destination bottom-right (x < x+scaledW → flip)
                    0, 0, // source top-left
                    frame.getWidth(null), frame.getHeight(null), // source bottom-right
                    null);
        } else {
            // normal draw
            g2d.drawImage(frame, x, y, scaledW, scaledH, null);
        }

        g2d.dispose();

        // --- Debug: render collision shape ---
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(2));

        // get collision shape in WORLD coordinates, then convert to SCREEN
        // Shape worldShape = this.getCollisionShape();
        // Rectangle2D r = worldShape.getBounds2D();

        // int screenX = (int) getGame().getCamera().toScreenX(r.getX());
        // int screenY = (int) getGame().getCamera().toScreenY(r.getY());
        // int screenW = (int) (r.getWidth() * getGame().getCamera().getZoom());
        // int screenH = (int) (r.getHeight() * getGame().getCamera().getZoom());

        // g2.drawRect(screenX, screenY, screenW, screenH);

        g2.dispose();
    }

    public void onCollision(BasicShapeCollidableEntity entity) {
        log.debug("Sprite entity collided with {}", entity);
    }

    @Override
    public void init() {
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void onCollision(Collidable entity) {
    }
}
