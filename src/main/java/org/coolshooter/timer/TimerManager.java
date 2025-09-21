package org.coolshooter.timer;

import java.util.ArrayList;
import java.util.List;

public class TimerManager {
    private final List<GameTimer> timers = new ArrayList<>();

    public void addTimer(GameTimer gameTimer) {
        this.timers.add(gameTimer);
    }

    public void update(double d) {
        timers.forEach(timer -> timer.update(d));
    }

    public void clearTimers() {
        this.timers.clear();
    }
}
