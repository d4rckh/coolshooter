package org.coolshooter.entity;

import org.coolshooter.Game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Entity {
    @Getter
    final Game game;

    @Getter
    private boolean destroyed = false;

    public abstract void init();

    public abstract void update(double delta);

    public void beforeDestroy() {};

    public void destroy() {
        beforeDestroy();
        this.destroyed = true;
    }
}
