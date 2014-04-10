Because the MOD Live doesn't have Wi-Fi or internet in any way, the device must
be connected to a computer running the Android Debug Bridge (ADB).
The computer will act as a bridge between the device and ardd (ARD daemon).

There are a few steps required to configure this on your machine.

1. Install the Android SDK onto your machine.
http://developer.android.com/sdk/index.html
2. Make sure adb is in your PATH, or navigate to your Android SDK's
`platform-tools` path and use `./adb` instead.
3. `socat`, or another equivilent tool, should be installed on your machine.
On Ubuntu, you can install it by running `sudo apt-get install socat`.

Before pressing "Testing mode" on Android app

    # Host uses port 3333, Device uses port 6666
    adb forward tcp:3333 tcp:6666

After pressing "Testing mode" in the app, but before the timeout occurs (10s).
Make sure ardd is running!

    # Relay data from two endpoints (in this case, two "servers")
    # 3333 is listening port on host, 4444 is ardd
    # Note - localhost:4444 can be replaced by another host if ardd is not on
    # the same machine
    socat TCP4:localhost:3333 TCP4:localhost:4444

