#!/usr/bin/env bash

export MYSQL_DB=OpticNavDB

./run_sql create_db.sql
./run_sql tables.sql
./run_sql Procedures/*.sql
./run_sql Triggers/*.sql

