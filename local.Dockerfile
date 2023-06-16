FROM maven:3-openjdk-17 AS MAVEN

MAINTAINER Antoine PRONNIER, <antoine.pronnier@gmail.com>

WORKDIR /container/funixproductions-api/

COPY pom.xml .

COPY client/pom.xml ./client/
COPY client/src ./client/src

COPY service/pom.xml ./service/
COPY service/src ./service/src

RUN mvn clean package -B
RUN rm service/target/funixproductions-api-service-*-javadoc.jar
RUN rm service/target/funixproductions-api-service-*-sources.jar

FROM openjdk:17 AS FINAL

WORKDIR /container/java

COPY --from=MAVEN /container/funixproductions-api/service/target/funixproductions-api-service-*.jar /container/java/server.jar
COPY gmail-credentials.json /container/java

ENTRYPOINT ["java", "-jar", "/container/java/server.jar", "-Dspring.profiles.active=docker"]
