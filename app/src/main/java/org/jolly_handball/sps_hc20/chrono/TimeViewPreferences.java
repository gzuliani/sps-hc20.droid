package org.jolly_handball.sps_hc20.chrono;

public class TimeViewPreferences {
    private boolean leadingZeroInMinutes = false;
    private boolean highResolutionLastMinute = true;

    public TimeViewPreferences() {
    }

    public TimeViewPreferences(boolean leadingZeroInMinutes, boolean highResolutionLastMinute) {
        this.leadingZeroInMinutes = leadingZeroInMinutes;
        this.highResolutionLastMinute = highResolutionLastMinute;
    }

    public boolean isLeadingZeroInMinutes() {
        return leadingZeroInMinutes;
    }

    public void setLeadingZeroInMinutes(boolean leadingZeroInMinutes) {
        this.leadingZeroInMinutes = leadingZeroInMinutes;
    }

    public boolean isHighResolutionLastMinute() {
        return highResolutionLastMinute;
    }

    public void setHighResolutionLastMinute(boolean highResolutionLastMinute) {
        this.highResolutionLastMinute = highResolutionLastMinute;
    }
}
