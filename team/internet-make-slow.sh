#!/usr/bin/env bash

sudo tc qdisc add dev eth0 root netem delay 500ms 50ms 25%
