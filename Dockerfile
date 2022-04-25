FROM openjdk:16-jdk-alpine
MAINTAINER Raslan
COPY ./target/wer-sitzt-wo-0.0.1-SNAPSHOT.jar wer-sitzt-wo.jar
ENTRYPOINT ["java", "-jar", "wer-sitzt-wo.jar"]
