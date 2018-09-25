package org.jolly_handball.sps_hc20;

import org.jolly_handball.sps_hc20.chrono.TimeViewPreferences;

class Preferences {

    private int minutesPerPeriod = 30;
    private boolean sirenOnPeriodEnd = true;
    private boolean sirenOnTimeoutCall = true;
    private boolean sirenOnTimeoutEnd = true;
    private TimeViewPreferences timeViewPreferences = new TimeViewPreferences();

    int getMinutesPerPeriod() {
        return minutesPerPeriod;
    }

    void setMinutesPerPeriod(int minutesPerPeriod) {
        this.minutesPerPeriod = minutesPerPeriod;
    }

    boolean isSirenOnPeriodEnd() {
        return sirenOnPeriodEnd;
    }

    void setSirenOnPeriodEnd(boolean sirenOnPeriodEnd) {
        this.sirenOnPeriodEnd = sirenOnPeriodEnd;
    }

    boolean isSirenOnTimeoutCall() {
        return sirenOnTimeoutCall;
    }

    void setSirenOnTimeoutCall(boolean sirenOnTimeoutCall) {
        this.sirenOnTimeoutCall = sirenOnTimeoutCall;
    }

    boolean isSirenOnTimeoutEnd() {
        return sirenOnTimeoutEnd;
    }

    void setSirenOnTimeoutEnd(boolean sirenOnTimeoutEnd) {
        this.sirenOnTimeoutEnd = sirenOnTimeoutEnd;
    }

    TimeViewPreferences getTimeViewPreferences() {
        return timeViewPreferences;
    }
}
