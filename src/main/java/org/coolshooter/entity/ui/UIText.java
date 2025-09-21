package org.coolshooter.entity.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.entity.common.RenderableEntity;

@Slf4j
public abstract class UIText extends RenderableEntity {
    @Setter
    @Getter
    private String text;

    public UIText(Game game, double worldX, double worldY) {
        super(game, worldX, worldY, false);
        this.text = "";
        setColor(Color.white);
    }

    /**
     * Render text at the screen position
     */
    @Override
    public void renderShape(Graphics g) {
        if (text == null || text.isEmpty()) return;


        g.setColor(getColor());
        g.setFont(new Font("Arial", Font.BOLD, 30)); // you can customize the font
        int x = (int) screenPosition.getX();
        int y = (int) screenPosition.getY();
        g.drawString(text, x, y);
    }

    @Override
    public void init() {
    }
}
