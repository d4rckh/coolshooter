package org.coolshooter.entity.player;

import javax.swing.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.gun.DefaultGun;
import org.coolshooter.entity.trait.Controllable;

import java.awt.*;
import java.awt.event.ActionEvent;

public class UserPlayerEntity extends PlayerEntity implements Controllable {
    private boolean shoot = false;

    // Normalized direction vector (−1..1, 0 if idle)
    private double dirX = 0;
    private double dirY = 0;

    public UserPlayerEntity(Game game) {
        super(game, 500, 500);
        setColor(Color.blue);
        setWidth(50);
        setSpeed(400);
        setHeight(50);
    }

    @Override
    public void init() {
        super.init();
        this.gun = new DefaultGun(this);
    }

    @Override
    public void update(double d) {
        super.update(d);

        if (shoot) {
            if (gun != null && getGame().getPanelMouse() != null) {
                Position mouseWorld = getGame().getCamera().toWorld(getGame().getPanelMouse());
                double dx = mouseWorld.getX() - (getPosition().getX() + this.getWidth() / 2);
                double dy = mouseWorld.getY() - (getPosition().getY() + this.getWidth() / 2);

                double length = Math.sqrt(dx * dx + dy * dy);
                if (length != 0) {
                    dx /= length;
                    dy /= length;
                }

                gun.shoot(dx, dy);
            }
        }
    }

    @Override
    public void registerKeyBindings(JPanel panel) {
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        // Mouse shooting
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                shoot = true;
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                shoot = false;
            }
        });

        // Movement flags
        final boolean[] upPressed = { false };
        final boolean[] downPressed = { false };
        final boolean[] leftPressed = { false };
        final boolean[] rightPressed = { false };

        // Update velocity and normalized direction
        Runnable updateVelocity = () -> {
            velX = 0;
            velY = 0;
            if (leftPressed[0])
                velX -= 1;
            if (rightPressed[0])
                velX += 1;
            if (upPressed[0])
                velY -= 1;
            if (downPressed[0])
                velY += 1;

            // Normalize and expose as dirX/dirY
            double len = Math.sqrt(velX * velX + velY * velY);
            if (len > 0) {
                dirX = velX / len;
                dirY = velY / len;
            } else {
                dirX = 0;
                dirY = 0;
            }
        };

        // Keyboard movement bindings
        im.put(KeyStroke.getKeyStroke("pressed W"), "upPressed");
        im.put(KeyStroke.getKeyStroke("released W"), "upReleased");
        im.put(KeyStroke.getKeyStroke("pressed S"), "downPressed");
        im.put(KeyStroke.getKeyStroke("released S"), "downReleased");
        im.put(KeyStroke.getKeyStroke("pressed A"), "leftPressed");
        im.put(KeyStroke.getKeyStroke("released A"), "leftReleased");
        im.put(KeyStroke.getKeyStroke("pressed D"), "rightPressed");
        im.put(KeyStroke.getKeyStroke("released D"), "rightReleased");

        am.put("upPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                upPressed[0] = true;
                updateVelocity.run();
            }
        });
        am.put("upReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                upPressed[0] = false;
                updateVelocity.run();
            }
        });
        am.put("downPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                downPressed[0] = true;
                updateVelocity.run();
            }
        });
        am.put("downReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                downPressed[0] = false;
                updateVelocity.run();
            }
        });
        am.put("leftPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed[0] = true;
                updateVelocity.run();
            }
        });
        am.put("leftReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed[0] = false;
                updateVelocity.run();
            }
        });
        am.put("rightPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed[0] = true;
                updateVelocity.run();
            }
        });
        am.put("rightReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed[0] = false;
                updateVelocity.run();
            }
        });
    }

    @Override
    public void renderShape(Graphics g) {
        super.renderShape(g);

        Position mousePanel = getGame().getPanelMouse();
        if (mousePanel != null) {
            // Convert player center to screen coordinates
            double playerCenterX = getPosition().getX() + getWidth() / 2.0;
            double playerCenterY = getPosition().getY() + getHeight() / 2.0;

            double screenX = getGame().getCamera().toScreenX(playerCenterX);
            double screenY = getGame().getCamera().toScreenY(playerCenterY);

            // Get normalized direction toward mouse in world coordinates
            Position mouseWorld = getGame().getCamera().toWorld(mousePanel);
            double dx = mouseWorld.getX() - playerCenterX;
            double dy = mouseWorld.getY() - playerCenterY;
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length > 0) {
                dx /= length;
                dy /= length;
            }

            // Scale line length for visualization
            double lineLength = 100; // pixels in world units
            double endX = playerCenterX + dx * lineLength;
            double endY = playerCenterY + dy * lineLength;

            // Convert end to screen coordinates
            double screenEndX = getGame().getCamera().toScreenX(endX);
            double screenEndY = getGame().getCamera().toScreenY(endY);

            // Draw aim line
            g.setColor(Color.YELLOW);
            g.drawLine((int) screenX, (int) screenY, (int) screenEndX, (int) screenEndY);

            // Optional: crosshair
            int crossSize = 8;
            g.drawLine((int) mousePanel.getX() - crossSize, (int) mousePanel.getY(),
                    (int) mousePanel.getX() + crossSize, (int) mousePanel.getY());
            g.drawLine((int) mousePanel.getX(), (int) mousePanel.getY() - crossSize,
                    (int) mousePanel.getX(), (int) mousePanel.getY() + crossSize);
        }
    }

    /**
     * Get the normalized direction vector (safe for bullet inheritance, AI, etc.)
     */
    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }
}
