package org.coolshooter.entity.ui;

import lombok.Getter;
import lombok.Setter;
import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.trait.Controllable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UISliderEntity extends RenderableEntity implements Controllable {
    private final int width;
    private final int height = 20;

    private boolean dragging = false;

    @Getter
    @Setter
    private double value = 0.5; // 0.0 - 1.0 normalized value

    public UISliderEntity(Game game, Position pos, int width) {
        super(game, pos.getX(), pos.getY(), false);
        this.width = width;
    }

    @Override
    public void init() {}

    @Override
    public void update(double delta) {
        // nothing for now
    }

    @Override
    public void renderShape(Graphics g) {
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY();

        // Track
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        // Progress
        g.setColor(Color.GRAY);
        g.fillRect(x, y, (int) (width * value), height);

        // Handle
        int handleX = x + (int) (width * value) - 5;
        g.setColor(Color.WHITE);
        g.fillRect(handleX, y - 5, 10, height + 10);
    }

    @Override
    public void registerKeyBindings(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isInside(e.getX(), e.getY())) {
                    dragging = true;
                    updateValue(e.getX());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    updateValue(e.getX());
                }
            }
        });
    }

    private boolean isInside(int mouseX, int mouseY) {
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY();
        return mouseX >= x && mouseX <= x + width &&
               mouseY >= y && mouseY <= y + height;
    }

    private void updateValue(int mouseX) {
        int x = (int) getScreenPosition().getX();
        double rel = (mouseX - x) / (double) width;
        value = Math.max(0, Math.min(1, rel));
    }
}
