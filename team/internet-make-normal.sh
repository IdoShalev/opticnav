#!/usr/bin/env bash

sudo tc qdisc del dev lo root netem delay 500ms

