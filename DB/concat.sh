#!/bin/bash

# Combine all SQL scripts into one
cat tables.sql Procedures/*.sql Triggers/*.sql
