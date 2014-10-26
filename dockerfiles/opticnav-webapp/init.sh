#!/bin/sh

mkdir -p /resources

export MYSQL_PARAMS="-h $MYSQL_PORT_3306_TCP_ADDR -P $MYSQL_PORT_3306_TCP_PORT \
-u$MYSQL_ENV_MYSQL_USER -p$MYSQL_ENV_MYSQL_PASSWORD $MYSQL_ENV_MYSQL_DATABASE"

# Initialize database if table doesn't exist
echo "desc WEB_ACCOUNT" | mysql $MYSQL_PARAMS > /dev/null 2>&1
if [ $? -ne 0 ]; then
    # MySQL had an error. Table probably doesn't exist.

    (mysql $MYSQL_PARAMS < concat.sql) || exit 1
fi

export JDBC_URL=jdbc\\:mysql\\://$MYSQL_PORT_3306_TCP_ADDR\\:$MYSQL_PORT_3306_TCP_PORT/$MYSQL_ENV_MYSQL_DATABASE

cat > /opticnav.properties << EOF
-Dopticnav.ardd.admin.host=$DAEMON_PORT_8888_TCP_ADDR
-Dopticnav.ardd.admin.port=$DAEMON_PORT_8888_TCP_PORT
-Dopticnav.jdbc.driverClassName=com.mysql.jdbc.Driver
-Dopticnav.jdbc.url=$JDBC_URL
-Dopticnav.jdbc.username=$MYSQL_ENV_MYSQL_USER
-Dopticnav.jdbc.password=$MYSQL_ENV_MYSQL_PASSWORD
-Dopticnav.resource.dir=/resources
EOF
