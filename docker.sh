#!/bin/bash
# Fail on error
set -e

mkdir -p dockerfiles/opticnav-{daemon,webapp}/build

gradle :ardd:distTar
cp ardd/ardd/build/distributions/ardd.tar dockerfiles/opticnav-daemon/build

gradle :web:war
cp web/build/libs/web.war dockerfiles/opticnav-webapp/build

(cd DB; ./concat.sh) > dockerfiles/opticnav-webapp/build/concat.sql
