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

    /*
     * Implements the init method for this entity
     */
    public abstract void init();

    /**
     * Implements the function to be called each frame for this entity
     * 
     * @param delta the time since last frame
     */
    public abstract void update(double delta);

    /**
     * This is called before the entity is destroyed, here all handles should be
     * released
     */
    public void beforeDestroy() {
    };

    /**
     * This function will call the beforeDestroy function and mark this entity as
     * destroyed, it will be removed by the entity manager on the next frame
     */
    public void destroy() {
        beforeDestroy();
        this.destroyed = true;
    }
}
