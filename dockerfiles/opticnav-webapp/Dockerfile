FROM java:openjdk-7

# Install Glassfish
RUN wget -q http://dlc.sun.com.edgesuite.net/glassfish/4.1/release/glassfish-4.1-web.zip && unzip glassfish-4.1*.zip && rm glassfish-4.1*.zip

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get install -y mysql-client && apt-get clean

# Add web archive
COPY build/web.war /web.war

ADD init.sh /init.sh
ADD start.sh /start.sh
ADD build/concat.sql /concat.sql

# Website
EXPOSE 8080

# Glassfish administration
# EXPOSE 4848

# Persistent data - map images
VOLUME '/resources'
ENV PATH /glassfish4/bin:$PATH

CMD ["/start.sh"]
