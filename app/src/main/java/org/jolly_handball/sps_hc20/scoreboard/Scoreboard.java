package org.jolly_handball.sps_hc20.scoreboard;

import org.jolly_handball.sps_hc20.UsbPort;

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
    private byte[] rxBuffer = null;

    private UsbPort usbPort;

    private int ackCount;
    private int nakCount;
    private int timeoutCount;
    private int errorCount;

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
        if ((lastTransmittedData != null)
                && (lastTransmittedData.equals(data)))
            return;

        txBuffer[1] = encodeDigit(units(data.getHomeSet()));
        txBuffer[2] = encodeDigit(min(1, hundreds(data.getHomeScore())));
        txBuffer[3] = encodeDigit(tens(data.getHomeScore()));
        txBuffer[4] = encodeDigit(units(data.getHomeScore()));

        int minute = data.getTimerLeftFigure();
        int second = data.getTimerRightFigure();
        int tensMinute = tens(minute);

        if (data.isLeadingZeroInMinutes())
            tensMinute = max(0, tensMinute);

        txBuffer[5] = encodeDigit(tensMinute);
        txBuffer[6] = encodeDigit(units(minute));
        txBuffer[7] = encodeDigit(max(0, tens(second)));
        txBuffer[8] = encodeDigit(units(second));
        txBuffer[9] = encodeDigit(min(1, hundreds(data.getGuestScore())));
        txBuffer[10] = encodeDigit(tens(data.getGuestScore()));
        txBuffer[11] = encodeDigit(units(data.getGuestScore()));
        txBuffer[12] = encodeDigit(units(data.getGuestSet()));

        if (data.isHomeSeventhFoul())
            txBuffer[2] |= (byte) 0x40;

        if (data.isHomeFirstTimeout())
            txBuffer[2] |= (byte) 0x20;

        if (data.isHomeSecondTimeout())
            txBuffer[2] |= (byte) 0x10;

        if (data.isSirenOn())
            txBuffer[5] |= (byte) 0x80;

        if (data.isTimerDotLit())
            txBuffer[6] |= (byte) 0x80;

        if (data.isGuestFirstTimeout())
            txBuffer[9] |= (byte) 0x40;

        if (data.isGuestSecondTimeout())
            txBuffer[9] |= (byte) 0x20;

        if (data.isGuestSeventhFoul())
            txBuffer[9] |= (byte) 0x10;

        usbPort.send(txBuffer, TIMEOUT);
        rxBuffer[0] = NUL;

        while (usbPort.receive(rxBuffer, TIMEOUT) > 0)
            ;

        if (rxBuffer[0] == NUL)
            timeoutCount += 1;

        else if (rxBuffer[0] == ACK) {
            ackCount += 1;
            lastTransmittedData = data;

        } else if (rxBuffer[0] == NAK)
            nakCount += 1;

        else
            errorCount += 1;
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
