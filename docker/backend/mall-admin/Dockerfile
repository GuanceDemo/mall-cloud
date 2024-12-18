FROM eclipse-temurin:8u382-b05-jre
MAINTAINER liurui

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

ENV jar app.jar
ENV workdir /data/app/
RUN mkdir -p ${workdir}
COPY .${jar} ${workdir}
WORKDIR ${workdir}
ENTRYPOINT ["sh", "-ec", "exec java ${JAVA_OPTS} -jar ${jar} ${PARAMS}"]