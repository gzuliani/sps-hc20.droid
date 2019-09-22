package org.jolly_handball.sps_hc20.scoreboard;

import org.jolly_handball.sps_hc20.UsbPort;

import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Scoreboard {
    static private final int DATA_LENGTH = 12;

    static private final byte NUL = 0x00;
    static private final byte STX = 0x02;
    static private final byte ETX = 0x03;
    static private final byte ACK = 0x06;
    static private final byte NAK = 0x15;

    static private final int TIMEOUT = 50; // ms

    private SevenSegmentsFont font = new SevenSegmentsFont();
    private Data lastTransmittedData = null;
    private byte[] txBuffer = new byte[DATA_LENGTH + 2];
    private byte[] rxBuffer;

    private UsbPort usbPort;

    private int ackCount;
    private int nakCount;
    private int timeoutCount;
    private int errorCount;

    private ScrollingText scrollingText = new ScrollingText(font);

    public Scoreboard(UsbPort port) {
        usbPort = port;

        prepareTxBuffer();
        rxBuffer = new byte[usbPort.getSuggestedInputBufferSize()];

        ackCount = 0;
        nakCount = 0;
        timeoutCount = 0;
        errorCount = 0;
    }

    public int getAckCount() {
        return ackCount;
    }

    public int getNakCount() {
        return nakCount;
    }

    public int getTimeoutCount() {
        return timeoutCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void update(Data data) {
        // the buffer that will contain the new data to transmit
        byte[] buffer = Arrays.copyOf(txBuffer, txBuffer.length);

        if ((lastTransmittedData == null)
                || (!lastTransmittedData.equals(data)))
        {
            buffer[1] = encodeDigit(units(data.getHomeSet()));
            buffer[2] = encodeDigit(min(1, hundreds(data.getHomeScore())));
            buffer[3] = encodeDigit(tens(data.getHomeScore()));
            buffer[4] = encodeDigit(units(data.getHomeScore()));

            int minute = data.getTimerLeftFigure();
            int second = data.getTimerRightFigure();
            int tensMinute = tens(minute);

            if (data.isLeadingZeroInMinutes())
                tensMinute = max(0, tensMinute);

            buffer[5] = encodeDigit(tensMinute);
            buffer[6] = encodeDigit(units(minute));
            buffer[7] = encodeDigit(max(0, tens(second)));
            buffer[8] = encodeDigit(units(second));
            buffer[9] = encodeDigit(min(1, hundreds(data.getGuestScore())));
            buffer[10] = encodeDigit(tens(data.getGuestScore()));
            buffer[11] = encodeDigit(units(data.getGuestScore()));
            buffer[12] = encodeDigit(units(data.getGuestSet()));

            if (data.isHomeSeventhFoul())
                buffer[2] |= (byte) 0x40;

            if (data.isHomeFirstTimeout())
                buffer[2] |= (byte) 0x20;

            if (data.isHomeSecondTimeout())
                buffer[2] |= (byte) 0x10;

            if (data.isSirenOn())
                buffer[5] |= (byte) 0x80;

            if (data.isTimerDotLit())
                buffer[6] |= (byte) 0x80;

            if (data.isGuestFirstTimeout())
                buffer[9] |= (byte) 0x40;

            if (data.isGuestSecondTimeout())
                buffer[9] |= (byte) 0x20;

            if (data.isGuestSeventhFoul())
                buffer[9] |= (byte) 0x10;
        }

        scrollingText.write(buffer);

        if (!Arrays.equals(buffer, txBuffer)) {
            usbPort.send(buffer, TIMEOUT);
            rxBuffer[0] = NUL;

            while (usbPort.receive(rxBuffer, TIMEOUT) > 0)
                ;

            if (rxBuffer[0] == NUL)
                timeoutCount += 1;

            else if (rxBuffer[0] == ACK) {
                ackCount += 1;
                lastTransmittedData = data;
                txBuffer = buffer;

            } else if (rxBuffer[0] == NAK)
                nakCount += 1;

            else
                errorCount += 1;
        }
    }

    public void showScrollingText(String text, int delay)
    {
        scrollingText.show(text, delay);
    }

    public void hideScrollingText()
    {
        scrollingText.hide();

        // force a display update
        Data data = lastTransmittedData;
        lastTransmittedData = null;
        update(data);
    }

    private void prepareTxBuffer() {
        txBuffer[0] = STX;

        for (int i = 0; i < DATA_LENGTH; i++)
            txBuffer[i + 1] = font.encode(' ');

        txBuffer[DATA_LENGTH + 1] = ETX;
    }

    private int units(int value) {
        return value % 10;
    }

    private int tens(int value) {
        return value < 10 ? -1 : (value / 10) % 10;
    }

    private int hundreds(int value) {
        return value < 100 ? -1 : (value / 100) % 10;
    }

    private byte encodeDigit(int value) {
        if (value < 0 || value > 9)
            return font.encode(' ');
        else
            return font.encode((char) (value + 0x30));
    }
}
