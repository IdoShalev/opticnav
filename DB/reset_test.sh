#!/usr/bin/env bash

export MYSQL_DB=OpticNavDB

export CONSTANTS=$(mktemp)
./constants > $CONSTANTS

./create.sh
./run_sql Data/*.sql

