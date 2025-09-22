package org.coolshooter.timer;

import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameTimer {
    private final Consumer<GameTimer> action;
    private final boolean repeat;

    private double elapsed;  // seconds

    // Fixed interval mode
    @Setter
    private double interval; // seconds
    private final Supplier<Double> intervalSupplier; // for dynamic interval

    /** Fixed interval constructor */
    public GameTimer(double interval, boolean repeat, Consumer<GameTimer> action) {
        this.interval = interval;
        this.repeat = repeat;
        this.action = action;
        this.intervalSupplier = null;
        this.elapsed = 0;
    }

    /** Dynamic interval constructor */
    public GameTimer(Supplier<Double> intervalSupplier, boolean repeat, Consumer<GameTimer> action) {
        this.intervalSupplier = intervalSupplier;
        this.repeat = repeat;
        this.action = action;
        this.elapsed = 0;
        this.interval = 0; // will be ignored
    }

    /** Call every frame with delta time in seconds */
    public void update(double delta) {
        elapsed += delta;
        double currentInterval = (intervalSupplier != null) ? intervalSupplier.get() : interval;

        if (elapsed >= currentInterval) {
            action.accept(this);
            if (repeat) {
                elapsed -= currentInterval; // carry over extra time
            } else {
                elapsed = currentInterval; // stop at max
            }
        }
    }

    /** Reset the timer */
    public void reset() {
        elapsed = 0;
    }

    /** Check if timer has reached its interval */
    public boolean isFinished() {
        double currentInterval = (intervalSupplier != null) ? intervalSupplier.get() : interval;
        return elapsed >= currentInterval;
    }
}
