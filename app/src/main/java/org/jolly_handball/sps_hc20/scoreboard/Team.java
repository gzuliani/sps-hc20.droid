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

    public boolean equals(Object obj) {
        if (!(obj instanceof Team))
            return false;
        return (score == ((Team) obj).score)
                && (set == ((Team) obj).set)
                && (isSeventhFoul == ((Team) obj).isSeventhFoul)
                && (isFirstTimeout == ((Team) obj).isFirstTimeout)
                && (isSecondTimeout == ((Team) obj).isSecondTimeout);
    }

    public int hashCode() {
        return 37
                + 7 * score
                + 11 * set
                + (isSeventhFoul ? 13 : 17)
                + (isFirstTimeout ? 19 : 23)
                + (isSecondTimeout ? 29 : 31);
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

    boolean isSecondTimeout() {
        return isSecondTimeout;
    }
}
