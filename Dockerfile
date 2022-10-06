FROM openjdk:8-jdk-alpine
COPY target/helloworld-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=qa", "-jar","/app.jar"]
