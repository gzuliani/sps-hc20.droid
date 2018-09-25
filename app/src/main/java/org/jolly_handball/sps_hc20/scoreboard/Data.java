package org.jolly_handball.sps_hc20.scoreboard;

public class Data {
    private Timer timer;
    private Team homeTeam;
    private Team guestTeam;
    private boolean isSirenOn;

    public Data(
            Timer timer,
            Team homeTeam,
            Team guestTeam,
            boolean isSirenOn) {
        this.timer = timer;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.isSirenOn = isSirenOn;
    }

    boolean isHomeSeventhFoul() {
        return homeTeam.isSeventhFoul();
    }

    boolean isHomeFirstTimeout() {
        return homeTeam.isFirstTimeout();
    }

    boolean isHomeSecondTimeout() {
        return homeTeam.isSecondTimeout();
    }

    int getHomeSet() {
        return homeTeam.getSet();
    }

    int getHomeScore() {
        return homeTeam.getScore();
    }

    boolean isGuestSeventhFoul() {
        return guestTeam.isSeventhFoul();
    }

    boolean isGuestFirstTimeout() {
        return guestTeam.isFirstTimeout();
    }

    boolean isGuestSecondTimeout() {
        return guestTeam.isSecondTimeout();
    }

    int getGuestSet() {
        return guestTeam.getSet();
    }

    int getGuestScore() {
        return guestTeam.getScore();
    }

    int getTimerLeftFigure() {
        return timer.getLeftFigure();
    }

    int getTimerRightFigure() {
        return timer.getRightFigure();
    }

    boolean isTimerDotLit() {
        return timer.isDotLit();
    }

    boolean isLeadingZeroInMinutes() {
        return timer.isLeadingZeroInMinutes();
    }

    boolean isSirenOn() {
        return isSirenOn;
    }
}
