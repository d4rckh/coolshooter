package org.coolshooter.timer;

import lombok.Setter;

import java.util.function.Consumer;

public class GameTimer {
    private final Consumer<GameTimer> action;
    /**
     * -- SETTER --
     * Set a new interval
     */
    @Setter
    private double interval; // seconds
    private double elapsed;  // seconds
    private final boolean repeat;

    /**
     * @param interval Time in seconds before triggering
     * @param repeat   If true, resets automatically after triggering
     * @param action   Code to run when timer triggers
     */
    public GameTimer(double interval, boolean repeat, Consumer<GameTimer> action) {
        this.interval = interval;
        this.repeat = repeat;
        this.action = action;
        this.elapsed = 0;
    }

    /**
     * Call every frame with delta time in seconds
     */
    public void update(double delta) {
        elapsed += delta;
        if (elapsed >= interval) {
            action.accept(this);
            if (repeat) {
                elapsed -= interval; // reset but carry over extra time
            } else {
                elapsed = interval; // stop at max
            }
        }
    }

    /**
     * Reset the timer
     */
    public void reset() {
        elapsed = 0;
    }

    /**
     * Check if timer has reached its interval
     */
    public boolean isFinished() {
        return elapsed >= interval;
    }
}
