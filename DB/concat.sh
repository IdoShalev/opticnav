#!/bin/bash

# Combine all SQL scripts into one
mkdir -p build
cat tables.sql Procedures/*.sql Triggers/*.sql > build/concat.sql
