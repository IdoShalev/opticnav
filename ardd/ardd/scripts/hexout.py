#!/usr/bin/env python3

import sys

def read_stdin():
    try:
        while True:
            buf = sys.stdin.buffer.read(1)
            if buf:
                code = buf[0]
                print("{:02x} ".format(code), end="")
                sys.stdout.flush()
            else:
                raise KeyboardInterrupt
    except KeyboardInterrupt:
        sys.stdout.flush()
        pass

read_stdin()

