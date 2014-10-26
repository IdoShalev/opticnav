FROM java:openjdk-7-jre

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get install -y mysql-client && apt-get clean

ENV TOMCAT_MAJOR_VERSION 8
ENV TOMCAT_MINOR_VERSION 8.0.14
ENV CATALINA_HOME /tomcat

# INSTALL TOMCAT
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_MINOR_VERSION}/bin/apache-tomcat-${TOMCAT_MINOR_VERSION}.tar.gz && \
    wget -qO- https://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_MINOR_VERSION}/bin/apache-tomcat-${TOMCAT_MINOR_VERSION}.tar.gz.md5 | md5sum -c - && \
    tar zxf apache-tomcat-*.tar.gz && \
    rm apache-tomcat-*.tar.gz && \
    mv apache-tomcat* tomcat

RUN rm -r /tomcat/webapps/*
COPY build/web.war /tomcat/webapps/ROOT.war

ADD init.sh /init.sh
ADD start.sh /start.sh
ADD build/concat.sql /concat.sql

# Website
EXPOSE 8080

# Persistent data - map images
VOLUME '/resources'

CMD ["/start.sh"]
