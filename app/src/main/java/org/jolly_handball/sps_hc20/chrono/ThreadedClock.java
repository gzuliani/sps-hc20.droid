package org.jolly_handball.sps_hc20.chrono;

public class ThreadedClock implements Runnable {

    private double ticksPerSecond;
    private long delayInMs;
    private Observer observer = null;
    private Thread thread = null;
    private boolean threadStopFlag = false;

    ThreadedClock(int resolution) {
        ticksPerSecond = Math.pow(10, -resolution);
        delayInMs = Math.round(1 / (ticksPerSecond * 2) * 1000);
    }

    void register(Observer observer) {
        this.observer = observer;
    }

    void startStop() {
        if (thread == null) {
            resetThreadStopFlag();
            thread = new Thread(this);
            thread.start();
        } else {
            setThreadStopFlag();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            thread = null;
        }
    }

    public void run() {
        long pointInTime = currentTimeTicks();

        while (!shouldThreadExit()) {
            long newPointInTime = currentTimeTicks();

            if (newPointInTime - pointInTime < 1) {
                try {
                    Thread.sleep(delayInMs);
                } catch (InterruptedException e) {
                }
            } else {
                if (observer != null) {
                    for (long i = 0; i < newPointInTime - pointInTime; i++)
                        if (!observer.tick()) {
                            setThreadStopFlag();
                            thread = null;
                        }
                }
                pointInTime = newPointInTime;
            }
        }
    }

    private long currentTimeTicks() {
        return Math.round(System.currentTimeMillis() / 1000. * ticksPerSecond);
    }

    private synchronized void setThreadStopFlag() {
        threadStopFlag = true;
    }

    private synchronized void resetThreadStopFlag() {
        threadStopFlag = false;
    }

    private synchronized boolean shouldThreadExit() {
        return threadStopFlag;
    }

    public interface Observer {
        boolean tick();
    }
}
