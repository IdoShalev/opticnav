#!/usr/bin/env python3

import sys

def read_stdin():
    hexcode = ""
    for line in sys.stdin:
        for c in line:
            c = c.upper()
            if c in "0123456789ABCDEF":
                hexcode = hexcode + c
            if len(hexcode) == 2:
                code = int(hexcode, 16)
                sys.stdout.buffer.write(bytearray([code]))
                hexcode = ""
        
        # only flush on every line
        # (every byte would be very inefficient to the program receiving input)
        sys.stdout.flush()

read_stdin()

