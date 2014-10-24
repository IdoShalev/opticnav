FROM java:openjdk-7-jre

ADD build/ardd.tar /

# Device port that devices use to connect
EXPOSE 4444

# Administration port that the Web app requires
EXPOSE 8888

WORKDIR /ardd/bin

CMD ["./ardd"]
