package org.coolshooter.entity.ui;

import lombok.Setter;
import org.coolshooter.Game;
import org.coolshooter.Position;
import org.coolshooter.entity.common.RenderableEntity;

import java.awt.*;

public class UILabelEntity extends RenderableEntity {
    @Setter
    private String text;

    @Setter
    private Font font = new Font("Arial", Font.PLAIN, 18);

    @Setter
    private Color color = Color.WHITE;

    public UILabelEntity(Game game, Position pos, String text) {
        super(game, pos.getX(), pos.getY(), false);
        this.text = text;
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

        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
    }
}
