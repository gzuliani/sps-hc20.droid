package org.jolly_handball.sps_hc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.jolly_handball.sps_hc20.chrono.HiResTime;
import org.jolly_handball.sps_hc20.chrono.Timer;
import org.jolly_handball.sps_hc20.chrono.TimerWidget;

public class TimeoutDialogFragment extends DialogFragment {

    Timer gameTimer = null;
    Timer timeoutTimer = null;
    TimerWidget timerWidget = null;

    public void setUp(Timer gameTimer, Timer timeoutTimer, TimerWidget timerWidget) {
        this.gameTimer = gameTimer;
        this.timeoutTimer = timeoutTimer;
        this.timerWidget = timerWidget;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String ok = getString(android.R.string.ok);
        String message = getString(R.string.timeout_dialog_message);
        HiResTime now = gameTimer.now();
        builder
                .setTitle(R.string.timeout_dialog_title)
                .setMessage(String.format(message, now.getMinute(), now.getSecond(), ok))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timeoutTimer.stop();
                        timerWidget.changeTimer(gameTimer);
                        gameTimer.start();
                    }
                });
        return builder.create();
    }
}
