This program depends on the "adb forward" command to communicate between
the host machine and the Android device.

Before running the program on the device, type into the Android Terminal:
    
    adb forward tcp:3333 tcp:6666

The above command binds two sockets, one local and one remote.

When the "Send Hello" button is pushed on the device, the program will block
until it accepts a client connection.

We can join the connection in the terminal. This must happen AFTER the button
is pressed!
    
    nc localhost 6666

After the above is entered, the program on the device will unblock and send
a message to the host machine.
