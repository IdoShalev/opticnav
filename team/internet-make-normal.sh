#!/usr/bin/env bash

sudo tc qdisc del dev eth0 root netem delay 500ms
