package org.jolly_handball.sps_hc20.chrono;

public class HiResTime {
    private int minute;
    private int second;
    private int tenthOfSecond;

    HiResTime(int minute, int second, int tenthOfSecond) {
        this.minute = minute;
        this.second = second;
        this.tenthOfSecond = tenthOfSecond;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HiResTime))
            return false;
        return (minute == ((HiResTime) obj).minute)
                && (second == ((HiResTime) obj).second)
                && (tenthOfSecond == ((HiResTime) obj).tenthOfSecond);
    }

    public int hashCode() {
        return 31 + 7 * minute + 11 * second + 13 * tenthOfSecond;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTenthOfSecond() {
        return tenthOfSecond;
    }

    Time toTime() {
        return new Time(minute, second);
    }
}
