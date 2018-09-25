package org.jolly_handball.sps_hc20;

class Team {
    private static final int minSet = 0;
    private static final int maxSet = 9;

    private static final int minScore = 0;
    private static final int maxScore = 199;

    private int set;
    private int score;
    private boolean seventhFoul;
    private boolean firstTimeout;
    private boolean secondTimeout;

    Team() {
        set = 0;
        score = 0;
        seventhFoul = false;
        firstTimeout = false;
        secondTimeout = false;
    }

    int getSet() {
        return set;
    }

    void incrementSet() {
        set += 1;

        if (set > maxSet)
            set = minSet;
    }

    void decrementSet() {
        set -= 1;
        if (set < minSet)
            set = maxSet;
    }

    int getScore() {
        return score;
    }

    void incrementScore() {
        score += 1;
        if (score > maxScore)
            score = minScore;
    }

    void decrementScore() {
        score -= 1;

        if (score < minScore)
            score = maxScore;
    }

    boolean isSeventhFoul() {
        return seventhFoul;
    }

    void setSeventhFoul(boolean value) {
        seventhFoul = value;
    }

    boolean isFirstTimeout() {
        return firstTimeout;
    }

    void setFirstTimeout(boolean value) {
        firstTimeout = value;
    }

    boolean isSecondTimeout() {
        return secondTimeout;
    }

    void setSecondTimeout(boolean value) {
        secondTimeout = value;
    }
}
