package org.jolly_handball.sps_hc20.chrono;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.jolly_handball.sps_hc20.AutoExpandingDialogFragment;
import org.jolly_handball.sps_hc20.R;

public class SetTimeDialogFragment extends AutoExpandingDialogFragment {

    private Time time = null;
    private Observer observer = null;
    private NumberPicker minutePicker = null;
    private NumberPicker secondPicker = null;

    void setUp(Time time, Observer observer) {
        this.time = time;
        this.observer = observer;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.set_time_dialog, null);
        builder.setView(view)
                .setTitle(R.string.set_time_dialog_title)
                .setMessage(R.string.set_time_dialog_message)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                observer.onTimeChanged(
                                        new Time(
                                                minutePicker.getValue(),
                                                secondPicker.getValue()));
                            }
                        })
                .setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SetTimeDialogFragment.this.getDialog().cancel();
                            }
                        });

        minutePicker = view.findViewById(R.id.set_time_minute);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(99);
        minutePicker.setValue(time.getMinute());

        secondPicker = view.findViewById(R.id.set_time_second);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setValue(time.getSecond());

        return builder.create();
    }

    public interface Observer {
        void onTimeChanged(Time time);
    }
}
