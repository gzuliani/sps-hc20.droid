package org.jolly_handball.sps_hc20;

import android.content.SharedPreferences;

import org.jolly_handball.sps_hc20.chrono.TimeViewPreferences;

class Preferences {

    public interface Observer {
        void onPreferencesChanged();
    }

    private int minutesPerPeriod = 30;
    private boolean sirenOnPeriodEnd = true;
    private boolean sirenOnTimeoutCall = true;
    private boolean sirenOnTimeoutEnd = true;
    private TimeViewPreferences timeViewPreferences = new TimeViewPreferences();
    private boolean showTransmissionStats = false;
    private Observer observer = null;

    int getMinutesPerPeriod() {
        return minutesPerPeriod;
    }

    boolean isSirenOnPeriodEnd() {
        return sirenOnPeriodEnd;
    }

    boolean isSirenOnTimeoutCall() {
        return sirenOnTimeoutCall;
    }

    boolean isSirenOnTimeoutEnd() {
        return sirenOnTimeoutEnd;
    }

    TimeViewPreferences getTimeViewPreferences() {
        return timeViewPreferences;
    }

    boolean isShowTransmissionStats() {
        return showTransmissionStats;
    }

    void registerObserver(Observer observer) {
        this.observer = observer;
    }

    void load(SharedPreferences preferences) {
        minutesPerPeriod = preferences.getInt("period_duration", 30);
        sirenOnPeriodEnd = preferences.getBoolean("siren_on_period_end", true);
        sirenOnTimeoutCall = preferences.getBoolean("siren_on_timeout_call", true);
        sirenOnTimeoutEnd = preferences.getBoolean("siren_on_timeout_end", true);
        timeViewPreferences.setLeadingZeroInMinutes(preferences.getBoolean("leading_zero_in_minutes", false));
        timeViewPreferences.setHighResolutionLastMinute(preferences.getBoolean("high_resolution_last_minute", true));
        showTransmissionStats = preferences.getBoolean("show_transmission_stats", false);

        if (observer != null)
            observer.onPreferencesChanged();
    }
}
