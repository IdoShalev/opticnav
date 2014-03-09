#!/usr/bin/env bash

sudo tc qdisc add dev lo root netem delay 500ms 50ms 25%
