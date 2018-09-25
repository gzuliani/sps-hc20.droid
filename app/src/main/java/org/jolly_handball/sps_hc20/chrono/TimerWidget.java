package org.jolly_handball.sps_hc20.chrono;

import android.app.FragmentManager;
import android.widget.Button;
import android.widget.TextView;

import org.jolly_handball.sps_hc20.R;

public class TimerWidget implements SetTimeDialogFragment.Observer {

    private Timer timer;
    private TextView timeWidget;
    private Button startStopButton;
    private Button setButton;
    private Button resetButton;

    private boolean wasTimerRunning;

    public TimerWidget(
            Timer timer,
            TextView timeWidget,
            Button startStopButton,
            Button setButton,
            Button resetButton) {
        this.timer = timer;
        this.timeWidget = timeWidget;
        this.startStopButton = startStopButton;
        this.setButton = setButton;
        this.resetButton = resetButton;
        wasTimerRunning = timer.isRunning();
    }

    public HiResTime now() {
        return timer.now();
    }

    public Figures figures() {
        return timer.figures();
    }

    public void startStop() {
        timer.startStop();
        wasTimerRunning = timer.isRunning();
        updateStartStopButtonText();
    }

    public void changeTimer(Timer timer) {
        this.timer = timer;
    }

    public void changeTime(FragmentManager fragmentManager) {
        SetTimeDialogFragment dialog = new SetTimeDialogFragment();
        dialog.setUp(timer.peek(), this);
        dialog.show(fragmentManager, "SetTime");
    }

    public void reset() {
        timer.reset();
        update();
    }

    public void update() {
        String text = timer.figuresAsText();

        if (!text.contentEquals(timeWidget.getText())) {
            timeWidget.setText(text);
        }

        updateStartStopButtonText();

        // did the timer change its state since the last time we checked?
        boolean isTimerRunning = timer.isRunning();

        if (wasTimerRunning != isTimerRunning) {
            wasTimerRunning = isTimerRunning;
            updateStartStopButtonText();
        }
    }

    private void updateStartStopButtonText() {
        boolean isTimerRunning = timer.isRunning();
        if (isTimerRunning)
            startStopButton.setText(R.string.timer_stop);
        else
            startStopButton.setText(R.string.timer_start);
        setButton.setEnabled(!isTimerRunning);
        resetButton.setEnabled(!isTimerRunning);
    }

    @Override
    public void onTimeChanged(Time time) {
        timer.set(time);
        update();
    }
}
