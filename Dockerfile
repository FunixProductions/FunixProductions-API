FROM openjdk:17-alpine

ARG service_name
ARG service_base_dir
ENV SERVICE_NAME=${service_name}
ENV SERVICE_BASE_DIR=${service_base_dir}
ENV APP_VERSION=1.2.2.5

WORKDIR /container/java

ADD ./modules/${SERVICE_BASE_DIR}/service/target/funixproductions-${SERVICE_NAME}-service-${APP_VERSION}.jar /container/java/service.jar

WORKDIR /container/app
ENTRYPOINT ["/bin/sh", "-c", "java -jar -Xms150M -XX:MaxRAMPercentage=95.0 /container/java/service.jar"]
