package org.coolshooter.entity.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;
import org.coolshooter.entity.trait.Controllable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Slf4j
public class UIInputFieldEntity extends RenderableEntity implements Controllable {
    @Getter
    private String text = "";

    @Setter
    private boolean focused = false;

    private final int width;
    private final int height = 30;

    public UIInputFieldEntity(Game game, Position pos, int width) {
        super(game, pos.getX(), pos.getY(), false);
        this.width = width;
        setColor(Color.WHITE);
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
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);

        // Border
        g.setColor(focused ? Color.YELLOW : Color.GRAY);
        g.drawRect(x, y, width, height);

        // Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(text, x + 5, y + (height - 10));

        // Caret (blinking cursor)
        if (focused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int caretX = x + 5 + g.getFontMetrics().stringWidth(text);
            g.drawLine(caretX, y + 5, caretX, y + height - 5);
        }
    }

    @Override
    public void registerKeyBindings(JPanel panel) {
        // Mouse: focus on click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = (int) getScreenPosition().getX();
                int y = (int) getScreenPosition().getY();
                focused = e.getX() >= x && e.getX() <= x + width &&
                        e.getY() >= y && e.getY() <= y + height;
            }
        });

        // Keyboard typing
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!focused)
                    return;
                char c = e.getKeyChar();
                if (Character.isISOControl(c))
                    return; // ignore control chars
                text += c;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!focused)
                    return;

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !text.isEmpty()) {
                    text = text.substring(0, text.length() - 1);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    focused = false; // lose focus on Enter
                }
            }
        });
    }
}
