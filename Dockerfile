FROM jdk17
LABEL author="dave"
VOLUME [ "/tmp" ]
COPY target/server-0.0.1-SNAPSHOT.jar /server.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/server.jar"]
