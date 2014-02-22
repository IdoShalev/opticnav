#!/usr/bin/env bash

# This script should be run periodically in order to ensure we're all running
# the same software.

sudo apt-get install \
mysql-workbench \
bless \
wireshark \
aptitude \

# Gradle is needed for building the project
# If gradle-1.11 is not available (returns error), install the PPA repo
sudo apt-get install gradle-1.11 || (sudo add-apt-repository ppa:cwchien/gradle && sudo apt-get update)

