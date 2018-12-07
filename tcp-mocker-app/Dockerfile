FROM openjdk:8u181-jre-alpine

MAINTAINER Andrey Saksonov <andrey.saksonov@payworks.com>

ARG JAR_FILE
ARG PORT=10001
ARG TCPMAPPINGSPATH=/var/lib/tcp-mocker/tcp-mappings

ADD target/${JAR_FILE} /opt/tcp-mocker/app.jar

ENV MOCKSERVER_PORT=${PORT} \
    MOCKSERVER_TCPMAPPINGSPATH=${TCPMAPPINGSPATH}

EXPOSE $PORT

VOLUME ["${TCPMAPPINGSPATH}"]

CMD ["java", "-jar", "/opt/tcp-mocker/app.jar"]
