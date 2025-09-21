package org.coolshooter.entity.ui;

import org.coolshooter.Game;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UIPlayerHealthText extends UIText {
    public UIPlayerHealthText(Game game) {
        super(game, 10, 30);
    }

    @Override
    public void update(double d) {
        this.setText("Player health: " + this.getGame().getUserPlayerEntity().getHp());
    }
}
