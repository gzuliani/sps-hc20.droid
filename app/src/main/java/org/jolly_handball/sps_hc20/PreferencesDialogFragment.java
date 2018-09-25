package org.jolly_handball.sps_hc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import org.jolly_handball.sps_hc20.chrono.TimeViewPreferences;

public class PreferencesDialogFragment extends AutoExpandingDialogFragment {

    private Preferences preferences = null;
    private TimeViewPreferences timeViewPreferences = null;
    private Observer observer = null;
    private NumberPicker durationPicker = null;
    private Switch sirenOnPeriodEnd = null;
    private Switch sirenOnTimeoutCall = null;
    private Switch sirenOnTimeoutEnd = null;
    private Switch leadingZeroInMinutes = null;
    private Switch highResolutionLastMinute = null;

    public void setUp(Preferences preferences, Observer observer) {
        this.preferences = preferences;
        this.timeViewPreferences = preferences.getTimeViewPreferences();
        this.observer = observer;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.preferences_dialog, null);
        builder.setView(view)
                .setTitle(R.string.preferences_dialog_title)
                .setMessage(R.string.preferences_dialog_message)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                preferences.setMinutesPerPeriod(durationPicker.getValue());
                                preferences.setSirenOnPeriodEnd(sirenOnPeriodEnd.isChecked());
                                preferences.setSirenOnTimeoutCall(sirenOnTimeoutCall.isChecked());
                                preferences.setSirenOnTimeoutEnd(sirenOnTimeoutEnd.isChecked());
                                timeViewPreferences.setHighResolutionLastMinute(highResolutionLastMinute.isChecked());
                                timeViewPreferences.setLeadingZeroInMinutes(leadingZeroInMinutes.isChecked());

                                if (observer != null)
                                    observer.preferencesChanged();
                            }
                        })
                .setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PreferencesDialogFragment.this.getDialog().cancel();
                            }
                        });

        durationPicker = view.findViewById(R.id.preferences_period_duration);
        durationPicker.setMinValue(0);
        durationPicker.setMaxValue(99);
        durationPicker.setValue(preferences.getMinutesPerPeriod());

        sirenOnPeriodEnd = view.findViewById(R.id.preferences_siren_on_period_end);
        sirenOnPeriodEnd.setChecked(preferences.isSirenOnPeriodEnd());

        sirenOnTimeoutCall = view.findViewById(R.id.preferences_siren_on_timeout_call);
        sirenOnTimeoutCall.setChecked(preferences.isSirenOnTimeoutCall());

        sirenOnTimeoutEnd = view.findViewById(R.id.preferences_siren_on_timeout_end);
        sirenOnTimeoutEnd.setChecked(preferences.isSirenOnTimeoutEnd());

        leadingZeroInMinutes = view.findViewById(R.id.preferences_leading_zero_in_minutes);
        leadingZeroInMinutes.setChecked(timeViewPreferences.isLeadingZeroInMinutes());

        highResolutionLastMinute = view.findViewById(R.id.preferences_high_resolution_last_minute);
        highResolutionLastMinute.setChecked(timeViewPreferences.isHighResolutionLastMinute());

        return builder.create();
    }

    public interface Observer {
        void preferencesChanged();
    }
}
