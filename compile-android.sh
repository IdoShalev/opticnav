#!/usr/bin/env bash

# Because I can't figure out Gradle right now, the .apk on the web site has to
# be manually updated. This makes that less frustrating.

gradle :ardroid:assembleDebug && \
cp ardroid/build/apk/ardroid-debug-unaligned.apk web/webapp/downloads/opticnav.apk

