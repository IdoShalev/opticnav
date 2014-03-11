#!/usr/bin/env bash

MYSQL_USER=root
MYSQL_PASS=password

# Concatenates all .sql files and feeds the output into MySQL
cat create_db.sql tables.sql Procedures/*.sql Triggers/*.sql | mysql --user=$MYSQL_USER --password=$MYSQL_PASS

