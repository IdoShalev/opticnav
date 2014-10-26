#!/bin/sh

mkdir -p /resources

export MYSQL_PARAMS="-h $MYSQL_PORT_3306_TCP_ADDR -P $MYSQL_PORT_3306_TCP_PORT \
-u$MYSQL_ENV_MYSQL_USER -p$MYSQL_ENV_MYSQL_PASSWORD $MYSQL_ENV_MYSQL_DATABASE"

# Ping the MySQL server until it comes up.
# Tools such as fig will run services in any order, not waiting for others to finish.

VALID_DB=0
# Try 60 times (1 minute)
for i in `seq 1 60`; do
    echo "Test database: Attempt $i"
    mysql $MYSQL_PARAMS < /dev/null
    if [ $? -eq 0 ]; then
        # Exit status iS OK, means we connected
        echo "Test database: Success"
        VALID_DB=1
        break
    fi
    sleep 1
done

if [ $VALID_DB -eq 0 ]; then
    echo "Could not connect to MySQL database"
    exit 1
fi

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
