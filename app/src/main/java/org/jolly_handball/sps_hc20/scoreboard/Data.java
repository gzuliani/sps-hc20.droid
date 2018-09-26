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

    public boolean equals(Object obj) {
        if (!(obj instanceof Data))
            return false;
        return (timer.equals(((Data) obj).timer))
                && (homeTeam.equals(((Data) obj).homeTeam))
                && (guestTeam.equals(((Data) obj).guestTeam))
                && (isSirenOn == ((Data) obj).isSirenOn);
    }

    public int hashCode() {
        return 31
                + 7 * timer.hashCode()
                + 11 * homeTeam.hashCode()
                + 13 * guestTeam.hashCode()
                + (isSirenOn ? 17 : 19);
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
