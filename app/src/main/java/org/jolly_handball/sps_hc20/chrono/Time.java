package org.jolly_handball.sps_hc20.chrono;

public class Time {
    private int minute;
    private int second;

    public Time(int minute, int second) {
        this.minute = minute;
        this.second = second;
    }

    int getMinute() {
        return minute;
    }

    int getSecond() {
        return second;
    }
}
