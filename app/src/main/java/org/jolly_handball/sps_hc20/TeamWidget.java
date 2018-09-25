package org.jolly_handball.sps_hc20;

import android.widget.Switch;
import android.widget.TextView;

class TeamWidget {
    private Team team;
    private TextView setWidget;
    private TextView scoreWidget;
    private Switch seventhFoulWidget;
    private Switch firstTimeoutWidget;
    private Switch secondTimeoutWidget;

    TeamWidget(
            Team team,
            TextView setWidget,
            TextView scoreWidget,
            Switch seventhFoulWidget,
            Switch firstTimeoutWidget,
            Switch secondTimeoutWidget) {
        this.team = team;
        this.setWidget = setWidget;
        this.scoreWidget = scoreWidget;
        this.seventhFoulWidget = seventhFoulWidget;
        this.firstTimeoutWidget = firstTimeoutWidget;
        this.secondTimeoutWidget = secondTimeoutWidget;
    }

    void update() {
        String setLabel = Integer.toString(team.getSet());

        if (!setLabel.contentEquals(setWidget.getText()))
            setWidget.setText(setLabel);

        String scoreLabel = Integer.toString(team.getScore());

        if (!scoreLabel.contentEquals(scoreWidget.getText()))
            scoreWidget.setText(scoreLabel);

        if (team.isSeventhFoul() != seventhFoulWidget.isChecked())
            seventhFoulWidget.setChecked(team.isSeventhFoul());

        if (team.isFirstTimeout() != firstTimeoutWidget.isChecked())
            firstTimeoutWidget.setChecked(team.isFirstTimeout());

        if (team.isSecondTimeout() != secondTimeoutWidget.isChecked())
            secondTimeoutWidget.setChecked(team.isSecondTimeout());
    }
}
