package org.jolly_handball.sps_hc20.chrono;

import java.util.concurrent.atomic.AtomicBoolean;

public class Trigger implements StopWatch.Trigger {

    private Time time = null;
    private AtomicBoolean isTriggerOn = null;

    Trigger(StopWatch stopWatch) {
        stopWatch.registerTrigger(this);
    }

    void arm(Time time, AtomicBoolean isTriggerOn) {
        this.time = time;
        this.isTriggerOn = isTriggerOn;
    }

    public boolean shouldStop(HiResTime hiResTime) {
        if (isTriggerOn == null)
            return false;
        if ((hiResTime.getMinute() == time.getMinute())
                && (hiResTime.getSecond() == time.getSecond())
                && (hiResTime.getTenthOfSecond() == 0)) {
            isTriggerOn.set(true);
        }
        return false;
    }
}
