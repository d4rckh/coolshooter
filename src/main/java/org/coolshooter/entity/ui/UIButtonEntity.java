package org.coolshooter.entity.ui;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import org.coolshooter.DynamicPosition;
import org.coolshooter.Game;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.trait.Controllable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class UIButtonEntity extends RenderableEntity implements Controllable {
    private final String label;
    private final Runnable onClick;

    private final int width = 200;
    private final int height = 50;
    private boolean hovered = false;

    public UIButtonEntity(Game game, DynamicPosition pos, String label, Runnable onClick) {
        super(game, pos);
        this.label = label;
        this.onClick = onClick;
    }

    private boolean isInside(int mouseX, int mouseY) {
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY();
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void init() {
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void renderShape(Graphics g) {
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY();

        // Background
        g.setColor(hovered ? Color.DARK_GRAY : Color.GRAY);
        g.fillRect(x, y, width, height);

        // Border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        // Label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(label)) / 2;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(label, textX, textY);
    }

    @Override
    public void registerKeyBindings(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isInside(e.getX(), e.getY()) && !isDestroyed()) {
                    onClick.run();
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hovered = isInside(e.getX(), e.getY());
            }
        });
    }

    @Override
    public void beforeDestroy() {

    }
}
