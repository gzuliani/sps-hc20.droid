package org.jolly_handball.sps_hc20.scoreboard;

public class Team {
    private int score;
    private int set;
    private boolean isSeventhFoul;
    private boolean isFirstTimeout;
    private boolean isSecondTimeout;

    public Team(
            int set,
            int score,
            boolean isSeventhFoul,
            boolean isFirstTimeout,
            boolean isSecondTimeout) {
        this.score = score;
        this.set = set;
        this.isSeventhFoul = isSeventhFoul;
        this.isFirstTimeout = isFirstTimeout;
        this.isSecondTimeout = isSecondTimeout;
    }

    int getScore() {
        return score;
    }

    int getSet() {
        return set;
    }

    boolean isSeventhFoul() {
        return isSeventhFoul;
    }

    boolean isFirstTimeout() {
        return isFirstTimeout;
    }

    boolean isSecondTimeout() { return isSecondTimeout; }
}
