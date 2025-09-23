package org.coolshooter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Camera {
    private int offsetX;
    private int offsetY;
    private double zoom = 1.0; // 1.0 = normal scale

    public Camera(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Convert world position to screen coordinates
     */
    public Position toScreen(Position worldPos) {
        int x = (int) ((worldPos.getX() - offsetX) * zoom);
        int y = (int) ((worldPos.getY() - offsetY) * zoom);
        return new Position(x, y);
    }
    
    public void toScreen(Position worldPos, Position screenPos) {
        int x = (int) ((worldPos.getX() - offsetX) * zoom);
        int y = (int) ((worldPos.getY() - offsetY) * zoom);
        screenPos.setX(x);
        screenPos.setY(y);
    }

    /**
     * Convert world position to screen coordinates
     */
    public double toScreenX(double x) {
        return ((x - offsetX) * zoom);
    }

    /**
     * Convert world position to screen coordinates
     */
    public double toScreenY(double y) {
        return ((y - offsetY) * zoom);
    }

    /**
     * Convert screen position to world coordinates
     */
    public Position toWorld(Position screenPos) {
        int x = (int) (screenPos.getX() / zoom + offsetX);
        int y = (int) (screenPos.getY() / zoom + offsetY);
        return new Position(x, y);
    }

    /**
     * Adjust zoom to fit entire world inside panel
     */
    public void adjustZoom(int panelWidth, int panelHeight, int worldWidth, int worldHeight) {
        double zoomX = (double) panelWidth / worldWidth;
        double zoomY = (double) panelHeight / worldHeight;
        this.zoom = Math.min(zoomX, zoomY);
    }

    /**
     * Center the camera on a given world position
     */
    public void centerOn(Position worldPos, int panelWidth, int panelHeight) {
        // Calculate offsets so that worldPos is at the center of the panel
        this.offsetX = (int) (worldPos.getX() - panelWidth / (2.0 * zoom));
        this.offsetY = (int) (worldPos.getY() - panelHeight / (2.0 * zoom));
    }
}
