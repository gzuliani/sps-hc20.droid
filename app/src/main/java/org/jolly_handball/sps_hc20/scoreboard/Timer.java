package org.jolly_handball.sps_hc20.scoreboard;

public class Timer {
    private int leftFigure;
    private int rightFigure;
    private boolean isDotLit;
    private boolean leadingZeroInMinutes;

    public Timer(
            int leftFigure,
            int rightFigure,
            boolean isDotLit,
            boolean leadingZeroInMinutes) {
        this.leftFigure = leftFigure;
        this.rightFigure = rightFigure;
        this.isDotLit = isDotLit;
        this.leadingZeroInMinutes = leadingZeroInMinutes;
    }

    int getLeftFigure() {
        return leftFigure;
    }

    int getRightFigure() {
        return rightFigure;
    }

    boolean isDotLit() {
        return isDotLit;
    }

    boolean isLeadingZeroInMinutes() {
        return leadingZeroInMinutes;
    }
}
