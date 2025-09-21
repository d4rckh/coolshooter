package org.coolshooter;

import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.coolshooter.entity.Entity;
import org.coolshooter.entity.common.RenderableEntity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JPanel {

    private final Game game;

    @Getter
    private Point mousePos;

    private BufferedImage bgImage;

    public GamePanel(Game game) {
        super();
        this.game = game;

        // Load background
        try {
            bgImage = ImageIO.read(getClass().getClassLoader().getResource("bg.png"));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Camera camera = game.getCamera();

        // Draw background
        if (bgImage != null && camera != null) {
            int imgWidth = (int) (bgImage.getWidth() * camera.getZoom());
            int imgHeight = (int) (bgImage.getHeight() * camera.getZoom());

            int offsetX = -(int) (camera.getOffsetX() * camera.getZoom()) % imgWidth;
            int offsetY = -(int) (camera.getOffsetY() * camera.getZoom()) % imgHeight;

            for (int x = offsetX - imgWidth; x < getWidth(); x += imgWidth) {
                for (int y = offsetY - imgHeight; y < getHeight(); y += imgHeight) {
                    g.drawImage(bgImage, x, y, imgWidth, imgHeight, null);
                }
            }
        }

        // Get immutable snapshot of entities from EntityManager
        List<Entity> snapshot = game.getEntityManager().getEntities();

        snapshot.stream()
                .filter(e -> e instanceof RenderableEntity)
                .map(e -> (RenderableEntity) e)
                .filter(e -> !e.isDestroyed())
                .forEach(e -> e.render(g));
    }

}
