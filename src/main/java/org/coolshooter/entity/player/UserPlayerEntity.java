package org.coolshooter.entity.player;

import javax.swing.*;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.trait.Controllable;

import java.awt.*;
import java.awt.event.ActionEvent;

public class UserPlayerEntity extends PlayerEntity implements Controllable {
    private boolean shoot = false;

    public UserPlayerEntity(Game game) {
        super(game, 500, 500);
        setColor(Color.CYAN);
        setWidth(50);
        setSpeed(500);
        setHeight(50);
    }

    @Override
    public void init() {
        super.init();

        this.gun.setBulletColor(Color.magenta);
    }

    @Override
    public void update(double d) {
        super.update(d);

        if (shoot) {
            if (gun != null && getGame().getPanelMouse() != null) {
                Position mouseWorld = getGame().getCamera().toWorld(getGame().getPanelMouse());
                double dx = mouseWorld.getX() - getPosition().getX();
                double dy = mouseWorld.getY() - getPosition().getY();
                double length = Math.sqrt(dx * dx + dy * dy);
                if (length != 0) {
                    dx /= length;
                    dy /= length;
                }

                // Add player velocity in the same direction
                double playerVelX = velX * getSpeed();
                double playerVelY = velY * getSpeed();

                // Scale player's velocity along the shooting direction
                double dot = dx * playerVelX + dy * playerVelY;
                double addedSpeedX = dx * dot / Math.sqrt(dx * dx + dy * dy);
                double addedSpeedY = dy * dot / Math.sqrt(dx * dx + dy * dy);

                gun.shoot(dx, dy, addedSpeedX, addedSpeedY); // <-- Pass extra velocity
            }
            shoot = false;
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

        // Update velocity based on flags
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
        };

        // Keyboard movement
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
}
