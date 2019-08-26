# SPS HC20 - droid edition

## Introduction

The *SPS HC20 - droid edition* is an Android app that controls the SPS HC20 scoreboard by means of the custom-made, Arduino-based interface described in "[Controllo di un tabellone segnapunti con Arduino](https://gzuliani.github.io/arduino/arduino-scoreboard.html)" (italian only). It is a porting of the **consolle.py** application included in the [SPS HC20 Suite](https://github.com/gzuliani/sps-hc20).

The *SPS HC20 - droid edition* can be used on any Android terminal with USB-OTG
support or as a stand-alone application on devices that do not support it, given that it won't be able to communicate with the interface circuit.

## Project structure

### `chrono` package

Time management module. The main objects in this packages are:

* **`ThreadedClock`**: sends a `tick` to the attached `Observer` every hundredth of a second from a background thread;
* **`StopWatch`**: measures the time counting the *ticks* that receives from the `ThreadedClock`;
* **`TimeView`**: determines the figures to be shown on the display;
* **`GamePeriod`**: defines the duration of a game period;
* **`Timer`**: controls the internal clock;
* **`TimerWidget`**: keeps the on-screen timer up to date with the internal clock.

### `scoreboard` package

Scoreboard management module. The main objects in this packages are:

* **`Timer`**: contains time-related data;
* **`Team`**: contains team-related data (set, score and other flags);
* **`Data`**: contains the whole dataset to be shown on the scoreboard;
* **`Scoreboard`**: manages the communication to/from the interface circuit.

### main package

The main objects in this package are:

* **`UsbPort`**: models a virtual serial port over USB;
* **`MainActivity`**: controls the application's UI.

The "[Comunicazione seriale Arduino/Android via USB](https://gzuliani.github.io/arduino/arduino-android-usb.html)" page (italian only) describes how the `UsbPort` has been implemented.

## Known issues

The scoreboard's siren may emit a short buzz when the application connects to the interface circuit because Arduino board's autoreset feature causes the firmware to restart everytime its serial port is opened (see for example "[Arduino reset after serial connection opened](https://github.com/pyserial/pyserial/issues/156)").

A simple work-around is to connect the scoreboard to the interface circuit when the application is already running:

 1. switch the Android terminal on;
 2. connect the Arduino board to the Android terminal with an USB OTG cable;
 3. Android will ask you if it should open the *SPS HC20* application: choose yes;
 4. connect the scoreboard to the Arduino board.

A more reliable solution would be disabling the autoreset feature (see
"[DisablingAutoResetOnSerialConnection](https://playground.arduino.cc/Main/DisablingAutoResetOnSerialConnection)" for details), but it has its drawbacks: it prevents any future firmware upload.

## Credits

Icons made by Freepik from www.flaticon.com.
