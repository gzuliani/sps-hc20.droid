package org.jolly_handball.sps_hc20.chrono;

import java.util.concurrent.atomic.AtomicBoolean;

public class Timer {

    private StopWatch stopWatch = new StopWatch();
    private GamePeriod gamePeriod = new GamePeriod(stopWatch);
    private AtomicBoolean isPeriodExpired = new AtomicBoolean(false);
    private Trigger trigger = new Trigger(stopWatch);
    private AtomicBoolean isTriggerOn = new AtomicBoolean(false);
    private TimeView timeView;

    public Timer(TimeViewPreferences preferences) {
        timeView = new TimeView(preferences);
    }

    public void configure(TimeViewPreferences preferences) {
        timeView.configure(preferences);
    }

    public void setPeriodDuration(int minutes) {
        gamePeriod.setDuration(minutes, isPeriodExpired);
    }

    public void armTrigger(Time time) {
        trigger.arm(time, isTriggerOn);
    }

    public boolean isRunning() {
        return stopWatch.isRunning();
    }

    public boolean isExpired() {
        return isPeriodExpired.getAndSet(false);
    }

    public boolean isTriggered() {
        return isTriggerOn.getAndSet(false);
    }

    public void start() {
        stopWatch.start();
    }

    public void stop() {
        stopWatch.stop();
    }

    public void startStop() {
        stopWatch.startStop();
    }

    public void set(Time time) {
        stopWatch.set(time);
    }

    public void reset() {
        stopWatch.reset();
    }

    public HiResTime now() {
        return stopWatch.now();
    }

    public Time peek() {
        return stopWatch.now().toTime();
    }

    public Figures figures() {
        return timeView.figures(stopWatch, gamePeriod);
    }

    public String figuresAsText() {
        return timeView.figuresAsText(stopWatch, gamePeriod);
    }
}
