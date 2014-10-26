#!/bin/sh

if [ ! -f /initialized ]; then
    /init.sh
    touch initialized
fi

cd /tomcat
export CATALINA_OPTS="$(cat /opticnav.properties)"
exec ./bin/catalina.sh run
