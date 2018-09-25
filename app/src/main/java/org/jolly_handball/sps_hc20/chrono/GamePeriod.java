package org.jolly_handball.sps_hc20.chrono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePeriod
        implements StopWatch.Trigger, StopWatch.Observer {

    private StopWatch stopWatch;
    private int duration = -1;
    private ArrayList<Integer> expiredPeriods = new ArrayList<>();
    private AtomicBoolean isPeriodExpired = null;

    GamePeriod(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
        this.stopWatch.registerTrigger(this);
        this.stopWatch.registerObserver(this);
        expiredPeriods.clear();
    }

    public boolean isPeriodLastMinute() {
        int minute = stopWatch.now().getMinute();
        return (periodElapsedTime(minute) % duration) == (duration - 1);
    }

    void setDuration(int minutes, AtomicBoolean isPeriodExpired) {
        this.duration = minutes;
        this.isPeriodExpired = isPeriodExpired;
    }

    public void timeChanged(HiResTime hiResTime) {
        int minute = hiResTime.getMinute();
        Iterator<Integer> i = expiredPeriods.iterator();

        while (i.hasNext()) {
            int value = i.next();

            if (value >= minute)
                i.remove();
        }
    }

    public boolean shouldStop(HiResTime hiResTime) {
        int minute = hiResTime.getMinute();

        if ((hiResTime.getTenthOfSecond() == 0)
                && (hiResTime.getSecond() == 0)
                && (isExpired(minute))) {

            if (isPeriodExpired != null)
                isPeriodExpired.set(true);

            expiredPeriods.add(minute);
            return true;
        }

        return false;
    }

    private boolean isExpired(int minute) {
        return (periodElapsedTime(minute) % duration) == 0;
    }

    private int periodElapsedTime(int minute) {
        if (expiredPeriods.isEmpty())
            return minute;
        else
            return minute - Collections.max(expiredPeriods);
    }
}
