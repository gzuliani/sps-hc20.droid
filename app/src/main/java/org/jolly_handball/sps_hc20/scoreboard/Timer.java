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

    public boolean equals(Object obj) {
        if (!(obj instanceof Timer))
            return false;
        return (leftFigure == ((Timer) obj).leftFigure)
                && (rightFigure == ((Timer) obj).rightFigure)
                && (isDotLit == ((Timer) obj).isDotLit)
                && (leadingZeroInMinutes == ((Timer) obj).leadingZeroInMinutes);
    }

    public int hashCode() {
        return 31
                + 7 * leftFigure
                + 11 * rightFigure
                + (isDotLit ? 13 : 17)
                + (leadingZeroInMinutes ? 19 : 23);
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
