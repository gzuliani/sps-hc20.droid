package org.jolly_handball.sps_hc20;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jolly_handball.sps_hc20.chrono.Figures;
import org.jolly_handball.sps_hc20.chrono.HiResTime;
import org.jolly_handball.sps_hc20.chrono.Time;
import org.jolly_handball.sps_hc20.chrono.TimeViewPreferences;
import org.jolly_handball.sps_hc20.chrono.Timer;
import org.jolly_handball.sps_hc20.chrono.TimerWidget;
import org.jolly_handball.sps_hc20.scoreboard.Data;
import org.jolly_handball.sps_hc20.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements PreferencesDialogFragment.Observer {

    static private int TIMEOUT_DURATION = 1; // minutes
    static private int SIREN_BLAST_DURATION = 1000; // milliseconds

    // handler to refresh the gui
    private final Handler guiUpdateScheduler = new Handler();
    private final int guiUpdateDelay = 20; // milliseconds
    Runnable guiUpdateProcess = null;

    Preferences preferences = new Preferences();

    // transmission statistics
    TextView commStatsPacketsSent = null;
    TextView commStatsPacketsRefused = null;
    TextView commStatsPacketsLost = null;
    TextView commStatsOtherErrors = null;

    // timeout
    LinearLayout timerLayout = null;
    LinearLayout timeoutLayout = null;
    TextView timeoutHint = null;

    // buttons
    Button sirenButton = null;
    Button timeoutButton = null;
    Button connectButton = null;

    // scoreboard-related data
    private Timer gameTimer = null;
    private Timer timeoutTimer = null;
    private TimerWidget timerWidget = null;
    private Team homeTeam = null;
    private TeamWidget homeTeamWidget = null;
    private Team guestTeam = null;
    private TeamWidget guestTeamWidget = null;
    private boolean isSirenOn = false;
    private Scoreboard scoreboard = null;

    // serial connection
    private UsbManager usbManager;
    private UsbPort usbPort = null;

    // listener for the ACTION_USB_DEVICE_DETACHED event
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (usbPort.isConnected()) {
                    usbPort.disconnect();
                    scoreboard = null;
                    Toast.makeText(getApplicationContext(),
                            R.string.arduino_board_disconnected,
                            Toast.LENGTH_LONG).show();
                }
                updateConnectButtonLabel();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.version)).setText(
                String.format(
                        getString(R.string.version_label),
                        BuildConfig.VERSION_NAME));

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        usbPort = new UsbPort(usbManager);

        // register the ACTION_USB_DEVICE_DETACHED event listener
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiver, filter);

        commStatsPacketsSent = findViewById(R.id.comm_stats_packets_sent);
        commStatsPacketsRefused = findViewById(R.id.comm_stats_packets_refused);
        commStatsPacketsLost = findViewById(R.id.comm_stats_packets_lost);
        commStatsOtherErrors = findViewById(R.id.comm_stats_other_errors);

        timerLayout = findViewById(R.id.timer_layout);
        timeoutLayout = findViewById(R.id.timeout_layout);
        timeoutHint = findViewById(R.id.timeout_hint);

        timerLayout.setVisibility(View.VISIBLE);
        timeoutLayout.setVisibility(View.GONE);

        /*
         * time management
         */
        preferences = new Preferences();

        gameTimer = new Timer(preferences.getTimeViewPreferences());
        gameTimer.setPeriodDuration(preferences.getMinutesPerPeriod());

        timeoutTimer = new Timer(new TimeViewPreferences(
                preferences.getTimeViewPreferences().isLeadingZeroInMinutes(),
                false));
        timeoutTimer.setPeriodDuration(TIMEOUT_DURATION);

        final Button timerStartStopButton =
                findViewById(R.id.timer_start_stop);
        timerStartStopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerWidget.startStop();
                    }
                });

        Button timerSetButton = findViewById(R.id.timer_set);
        timerSetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerWidget.changeTime(getFragmentManager());
                    }
                });

        final Button timerResetButton = findViewById(R.id.timer_reset);
        timerResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timerWidget.reset();
                    }
                });

        final Button timeoutRestartButton = findViewById(R.id.timeout_restart);
        timeoutRestartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeoutTimer.stop();
                        timerWidget.changeTimer(gameTimer);
                        gameTimer.start();
                        timerLayout.setVisibility(View.VISIBLE);
                        timeoutLayout.setVisibility(View.GONE);
                    }
                });


        timerWidget = new TimerWidget(
                gameTimer,
                (TextView) findViewById(R.id.timer),
                timerStartStopButton,
                timerSetButton,
                timerResetButton);

        /*
         * home team
         */
        final Switch homeSeventhFoulSwitch =
                findViewById(R.id.home_seventh_foul);

        final Switch homeFirstTimeoutSwitch =
                findViewById(R.id.home_first_timeout);

        final Switch homeSecondTimeoutSwitch =
                findViewById(R.id.home_second_timeout);

        homeTeam = new Team();

        homeTeamWidget = new TeamWidget(
                homeTeam,
                (TextView) findViewById(R.id.home_set),
                (TextView) findViewById(R.id.home_score),
                homeSeventhFoulSwitch,
                homeFirstTimeoutSwitch,
                homeSecondTimeoutSwitch);

        homeTeamWidget.update();

        final Button homeSetIncrementButton =
                findViewById(R.id.home_set_increment);
        homeSetIncrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeTeam.incrementSet();
                        homeTeamWidget.update();
                    }
                });

        final Button homeSetDecrementButton =
                findViewById(R.id.home_set_decrement);
        homeSetDecrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeTeam.decrementSet();
                        homeTeamWidget.update();
                    }
                });

        final Button homeScoreIncrementButton =
                findViewById(R.id.home_score_increment);
        homeScoreIncrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeTeam.incrementScore();
                        homeTeamWidget.update();
                    }
                });

        final Button homeScoreDecrementButton =
                findViewById(R.id.home_score_decrement);
        homeScoreDecrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeTeam.decrementScore();
                        homeTeamWidget.update();
                    }
                });

        homeSeventhFoulSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        homeTeam.setSeventhFoul(isChecked);
                    }
                });

        homeFirstTimeoutSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        homeTeam.setFirstTimeout(isChecked);
                    }
                });

        homeSecondTimeoutSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        homeTeam.setSecondTimeout(isChecked);
                    }
                });

        /*
         * guest team
         */

        final Switch guestSeventhFoulSwitch =
                findViewById(R.id.guest_seventh_foul);

        final Switch guestFirstTimeoutSwitch =
                findViewById(R.id.guest_first_timeout);

        final Switch guestSecondTimeoutSwitch =
                findViewById(R.id.guest_second_timeout);

        guestTeam = new Team();

        guestTeamWidget = new TeamWidget(
                guestTeam,
                (TextView) findViewById(R.id.guest_set),
                (TextView) findViewById(R.id.guest_score),
                guestSeventhFoulSwitch,
                guestFirstTimeoutSwitch,
                guestSecondTimeoutSwitch);

        guestTeamWidget.update();

        final Button guestSetIncrementButton =
                findViewById(R.id.guest_set_increment);
        guestSetIncrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guestTeam.incrementSet();
                        guestTeamWidget.update();
                    }
                });

        final Button guestSetDecrementButton =
                findViewById(R.id.guest_set_decrement);
        guestSetDecrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guestTeam.decrementSet();
                        guestTeamWidget.update();
                    }
                });

        final Button guestScoreIncrementButton =
                findViewById(R.id.guest_score_increment);
        guestScoreIncrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guestTeam.incrementScore();
                        guestTeamWidget.update();
                    }
                });

        final Button guestScoreDecrementButton =
                findViewById(R.id.guest_score_decrement);
        guestScoreDecrementButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guestTeam.decrementScore();
                        guestTeamWidget.update();
                    }
                });

        guestSeventhFoulSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        guestTeam.setSeventhFoul(isChecked);
                    }
                });

        guestFirstTimeoutSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        guestTeam.setFirstTimeout(isChecked);
                    }
                });

        guestSecondTimeoutSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        guestTeam.setSecondTimeout(isChecked);
                    }
                });

        /*
         * command buttons
         */

        final Button configureButton = findViewById(R.id.configure);
        configureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesDialogFragment dialog = new PreferencesDialogFragment();
                dialog.setUp(preferences, MainActivity.this);
                dialog.show(getFragmentManager(), "Preferences");
            }
        });

        connectButton = findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usbPort.isConnected()) {
                    scoreboard = null;
                    usbPort.disconnect();
                } else {
                    HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
                    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                    while (deviceIterator.hasNext()) {
                        UsbDevice usbDevice = deviceIterator.next();
                        if (usbDevice.getVendorId() == 9025) {
                            usbPort.connect(usbDevice);
                            break;
                        }
                    }
                }

                if (usbPort.isConnected()) {
                    scoreboard = new Scoreboard(usbPort);
                    Toast.makeText(getApplicationContext(),
                            R.string.arduino_board_connected,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.arduino_board_not_found,
                            Toast.LENGTH_LONG).show();
                }

                updateConnectButtonLabel();
            }
        });

        updateConnectButtonLabel();

        sirenButton = findViewById(R.id.siren);

        timeoutButton = findViewById(R.id.timeout);
        timeoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameTimer.stop();

                if (preferences.isSirenOnTimeoutCall())
                    activateSiren();

                timeoutTimer.reset();
                timeoutTimer.armTrigger(new Time(0, 50));
                timerWidget.changeTimer(timeoutTimer);
                timeoutTimer.start();

                timeoutHint.setText(String.format(getString(R.string.timeout_hint), gameTimer.figuresAsText()));

                timerLayout.setVisibility(View.GONE);
                timeoutLayout.setVisibility(View.VISIBLE);
            }
        });

        // open the configuration window
        configureButton.callOnClick();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String action = intent.getAction();

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            usbPort.connect((UsbDevice) intent
                    .getParcelableExtra(UsbManager.EXTRA_DEVICE));
            scoreboard = new Scoreboard(usbPort);
            updateConnectButtonLabel();
        }

        guiUpdateScheduler.postDelayed(guiUpdateProcess = new Runnable() {
            public void run() {

                if (timerWidget != null) {
                    timerWidget.update();

                    if (gameTimer.isExpired())
                        periodExpired();

                    if (timeoutTimer.isTriggered())
                        timeoutExpiring();

                    if (timeoutTimer.isExpired())
                        timeoutExpired();

                    timeoutButton.setEnabled(gameTimer.isRunning());

                    if (scoreboard != null) {
                        HiResTime now = timerWidget.now();
                        Figures figures = timerWidget.figures();
                        scoreboard.update(
                                new Data(
                                        new org.jolly_handball.sps_hc20.scoreboard.Timer(
                                                figures.getLeft(),
                                                figures.getRight(),
                                                now.getTenthOfSecond() < 5,
                                                preferences.getTimeViewPreferences().isLeadingZeroInMinutes()),
                                        new org.jolly_handball.sps_hc20.scoreboard.Team(
                                                homeTeam.getSet(),
                                                homeTeam.getScore(),
                                                homeTeam.isSeventhFoul(),
                                                homeTeam.isFirstTimeout(),
                                                homeTeam.isSecondTimeout()),
                                        new org.jolly_handball.sps_hc20.scoreboard.Team(guestTeam.getSet(),
                                                guestTeam.getScore(),
                                                guestTeam.isSeventhFoul(),
                                                guestTeam.isFirstTimeout(),
                                                guestTeam.isSecondTimeout()),
                                        isSirenOn || sirenButton.isPressed()));

                        if (preferences.isShowTransmissionStats()) {
                            commStatsPacketsSent.setText(String.format(getString(R.string.comm_stats_packets_sent), Integer.toString(scoreboard.getAckCount())));
                            commStatsPacketsRefused.setText(String.format(getString(R.string.comm_stats_packets_refused), Integer.toString(scoreboard.getNakCount())));
                            commStatsPacketsLost.setText(String.format(getString(R.string.comm_stats_packets_lost), Integer.toString(scoreboard.getTimeoutCount())));
                            commStatsOtherErrors.setText(String.format(getString(R.string.comm_stats_other_errors), Integer.toString(scoreboard.getErrorCount())));
                        }
                    }
                }

                guiUpdateScheduler.postDelayed(guiUpdateProcess, guiUpdateDelay);
            }
        }, guiUpdateDelay);
    }

    public void preferencesChanged() {
        gameTimer.configure(preferences.getTimeViewPreferences());
        gameTimer.setPeriodDuration(preferences.getMinutesPerPeriod());

        if (preferences.isShowTransmissionStats()) {
            commStatsPacketsSent.setVisibility(View.VISIBLE);
            commStatsPacketsSent.setText(String.format(getString(R.string.comm_stats_packets_sent), Integer.toString(0)));
            commStatsPacketsRefused.setVisibility(View.VISIBLE);
            commStatsPacketsRefused.setText(String.format(getString(R.string.comm_stats_packets_refused), Integer.toString(0)));
            commStatsPacketsLost.setVisibility(View.VISIBLE);
            commStatsPacketsLost.setText(String.format(getString(R.string.comm_stats_packets_lost), Integer.toString(0)));
            commStatsOtherErrors.setVisibility(View.VISIBLE);
            commStatsOtherErrors.setText(String.format(getString(R.string.comm_stats_other_errors), Integer.toString(0)));
        } else {
            commStatsPacketsSent.setVisibility(View.INVISIBLE);
            commStatsPacketsRefused.setVisibility(View.INVISIBLE);
            commStatsPacketsLost.setVisibility(View.INVISIBLE);
            commStatsOtherErrors.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        guiUpdateScheduler.removeCallbacks(guiUpdateProcess);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_quit)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExitActivity.exitApplication(MainActivity.this);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void updateConnectButtonLabel() {
        if (scoreboard != null) {
            connectButton.setText(R.string.disconnect);
        } else {
            connectButton.setText(R.string.connect);
        }
    }

    private void periodExpired() {
        if (preferences.isSirenOnPeriodEnd())
            activateSiren();
    }

    private void timeoutExpiring() {
        if (preferences.isSirenOnTimeoutEnd())
            activateSiren();
    }

    private void timeoutExpired() {
        timerWidget.changeTimer(gameTimer);
    }

    private void activateSiren() {
        sirenOn();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        sirenOff();
                    }
                },
                SIREN_BLAST_DURATION);
    }

    private void sirenOn() {
        isSirenOn = true;
    }

    private void sirenOff() {
        isSirenOn = false;
    }
}
