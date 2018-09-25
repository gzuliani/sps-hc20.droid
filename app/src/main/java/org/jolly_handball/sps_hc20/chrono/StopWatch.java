package org.jolly_handball.sps_hc20.chrono;

import java.util.ArrayList;
import java.util.List;

public class StopWatch implements ThreadedClock.Observer {

    // set clock resolution to 1 cent of a second
    private final int resolution = -2;
    private final double ticksPerSecond = Math.pow(10, -resolution);
    private ThreadedClock clock = new ThreadedClock(resolution);
    private boolean isRunning;
    private long tickCount = 0;
    private HiResTime currentHiResTime;
    private Observer observer = null;
    private List<Trigger> triggers = new ArrayList<>();

    StopWatch() {
        isRunning = false;
        clock.register(this);
        setTickCount(0);
    }

    void registerObserver(Observer observer) {
        this.observer = observer;
    }

    void registerTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    boolean isRunning() {
        return isRunning;
    }

    public void start() {
        if (!isRunning)
            startStop();
    }

    void stop() {
        if (isRunning)
            startStop();
    }

    void startStop() {
        clock.startStop();
        isRunning = !isRunning;
    }

    void set(Time time) {
        setTickCount(
                (long) ((time.getMinute() * 60 + time.getSecond())
                        * ticksPerSecond));
        if (observer != null)
            observer.timeChanged(currentHiResTime);
    }

    void reset() {
        set(new Time(0, 0));
    }

    public HiResTime now() {
        return currentHiResTime;
    }

    public boolean tick() {
        HiResTime hiResTime = ticksToTime();

        if ((hiResTime.getMinute() == 99)
                && (hiResTime.getSecond() == 59)
                && (hiResTime.getTenthOfSecond() == 9)) {
            isRunning = false;
            return false;
        }

        tickCount++;
        HiResTime now = ticksToTime();

        if (!now.equals(currentHiResTime)) {
            currentHiResTime = now;
            for (Trigger trigger : triggers) {
                if (trigger.shouldStop(currentHiResTime)) {
                    isRunning = false;
                    return false;
                }
            }
        }

        return true;
    }

    private void setTickCount(long tickCount) {
        this.tickCount = tickCount;
        currentHiResTime = ticksToTime();
    }

    private HiResTime ticksToTime() {
        double elapsed = tickCount / ticksPerSecond;
        return new HiResTime(
                (int) (elapsed / 60),
                (int) (elapsed) % 60,
                (int) (10 * elapsed) % 10);
    }

    public interface Observer {
        void timeChanged(HiResTime hiResTime);
    }

    public interface Trigger {
        boolean shouldStop(HiResTime hiResTime);
    }
}
