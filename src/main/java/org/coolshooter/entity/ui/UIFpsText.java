package org.coolshooter.entity.ui;

import org.coolshooter.Game;

public class UIFpsText extends UIText {

    private int frames = 0;
    private double fps = 0;
    private long fpsTimer = System.nanoTime();

    public UIFpsText(Game game) {
        super(game, 10, 80);
    }

    @Override
    public void update(double delta) {
        // Update FPS
        frames++;
        long now = System.nanoTime();
        if (now - fpsTimer >= 1_000_000_000L) { // 1 second
            fps = frames;
            frames = 0;
            fpsTimer = now;
        }

        // Set text including health and FPS
        this.setText("FPS: " + (int) fps);
    }
}
