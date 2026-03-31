FROM openjdk:17-jdk-alpine
COPY target/store.jar store.jar
ENTRYPOINT ["java", "-jar", "/app_api.jar"]
