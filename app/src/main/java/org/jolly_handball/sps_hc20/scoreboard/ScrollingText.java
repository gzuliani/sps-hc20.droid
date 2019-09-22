package org.jolly_handball.sps_hc20.scoreboard;

class ScrollingText {

    // clock displays
    final private static int[] displays = {5, 6, 7, 8};

    private byte[] encodedText = {};
    private int delay = 0;
    private int cursorPos = 0;
    private long lastWriteTime = 0;
    private boolean isActive = false;
    private SevenSegmentsFont font;
    private String padding = new String(new char[displays.length]).replace("\0", " ");

    ScrollingText(SevenSegmentsFont font)
    {
        this.font = font;
    }

    void show(String text, int delay)
    {
        encodedText = font.encode(String.format("%s%s%s", padding, text, padding));

        this.delay = delay;
        cursorPos = 0;
        isActive = true;
    }

    void hide()
    {
        isActive = false;
    }

    void write(byte[] buffer)
    {
        if (!isActive)
            return;

        for (int i = 0; i < displays.length; i++)
            buffer[displays[i]] = encodedText[cursorPos + i];

        long now = System.currentTimeMillis();

        if (now - lastWriteTime > delay) {
            cursorPos = (cursorPos + 1) % (encodedText.length - padding.length());
            lastWriteTime = now;
        }
    }
}
