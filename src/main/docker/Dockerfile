FROM frolvlad/alpine-java

VOLUME /tmp

ADD mlgia-text-to-speech-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]