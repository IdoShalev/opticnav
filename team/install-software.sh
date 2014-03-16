#!/usr/bin/env bash

# This script should be run periodically in order to ensure we're all running
# the same software.

sudo apt-get install \
mysql-workbench \
bless \
wireshark \
aptitude \
dia \

# Gradle is needed for building the project
# If gradle-1.10 is not available (returns error), install the PPA repo
sudo apt-get install gradle-1.10 || (sudo add-apt-repository ppa:cwchien/gradle && sudo apt-get update && sudo apt-get install gradle-1.10)
# Gradle 1.11 should be removed, as it doesn't work with the Android gradle tools
sudo apt-get remove gradle-1.11
