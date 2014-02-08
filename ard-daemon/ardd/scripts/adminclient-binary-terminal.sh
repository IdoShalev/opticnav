#!/usr/bin/env bash

# convert input hex to binary
# connect to localhost:8888, the AdminClient port
# convert output binary to hex

./hexin.py | nc localhost 8888 | ./hexout.py

