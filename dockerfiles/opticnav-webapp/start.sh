#!/bin/bash

if [ ! -f /initialized ]; then
    /init.sh
    touch initialized
fi

exec asadmin start-domain -v -w
