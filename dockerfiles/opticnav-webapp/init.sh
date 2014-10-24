#!/bin/bash

mkdir -p /resources

export MYSQL_PARAMS="-h $MYSQL_PORT_3306_TCP_ADDR -P $MYSQL_PORT_3306_TCP_PORT \
-u$MYSQL_ENV_MYSQL_USER -p$MYSQL_ENV_MYSQL_PASSWORD $MYSQL_ENV_MYSQL_DATABASE"

# Initialize database if table doesn't exist
echo "desc WEB_ACCOUNT" | mysql $MYSQL_PARAMS > /dev/null 2>&1
if [ $? -ne 0 ]; then
    # MySQL had an error. Table probably doesn't exist.

    (mysql $MYSQL_PARAMS < concat.sql) || exit 1
fi

# Each colon (such as those in URLs) needs to be escaped. 
# each property for create-system-properties is delimited by colons.
export JDBC_URL=jdbc\\:mysql\\://$MYSQL_PORT_3306_TCP_ADDR\\:$MYSQL_PORT_3306_TCP_PORT/$MYSQL_ENV_MYSQL_DATABASE

asadmin start-domain

asadmin create-system-properties \
opticnav.ardd.admin.host=$DAEMON_PORT_8888_TCP_ADDR:\
opticnav.ardd.admin.port=$DAEMON_PORT_8888_TCP_PORT:\
opticnav.jdbc.driverClassName=com.mysql.jdbc.Driver:\
opticnav.jdbc.url=$JDBC_URL:\
opticnav.jdbc.username=$MYSQL_ENV_MYSQL_USER:\
opticnav.jdbc.password=$MYSQL_ENV_MYSQL_PASSWORD:\
opticnav.resource.dir=/resources


# Deploy web.war to Glassfish
asadmin deploy --contextroot / /web.war

asadmin stop-domain