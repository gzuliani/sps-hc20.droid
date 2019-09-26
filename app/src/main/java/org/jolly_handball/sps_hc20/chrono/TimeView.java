package org.jolly_handball.sps_hc20.chrono;

class TimeView {

    private String timeLayout;
    private TimeViewPreferences preferences = null;

    TimeView(TimeViewPreferences preferences) {
        configure(preferences);
    }

    Figures figures(StopWatch stopWatch, GamePeriod gamePeriod) {
        HiResTime now = stopWatch.now();
        if (preferences.isHighResolutionLastMinute()
                && gamePeriod.isPeriodLastMinute())
            return new Figures(
                    now.getSecond(),
                    now.getTenthOfSecond() * 10);
        else
            return new Figures(now.getMinute(), now.getSecond());
    }

    String figuresAsText(StopWatch stopWatch, GamePeriod gamePeriod) {
        Figures figures = figures(stopWatch, gamePeriod);
        return String.format(timeLayout, figures.getLeft(), figures.getRight());
    }

    void configure(TimeViewPreferences preferences) {
        this.preferences = preferences;

        if (this.preferences.isLeadingZeroInMinutes())
            timeLayout = "%02d:%02d"; // leading zero
        else
            timeLayout = "%2d:%02d"; // no leading zero
    }
}
