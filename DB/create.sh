#!/usr/bin/env bash

export MYSQL_DB=OpticNavDB

export CONSTANTS=$(mktemp)
./constants > $CONSTANTS

./run_sql create_db.sql
./run_sql tables.sql
./run_sql Procedures/*.sql
./run_sql Triggers/*.sql

