#!/usr/bin/env bash

export MYSQL_DB=OpticNavDB

./create.sh
./run_sql Data/*.sql

