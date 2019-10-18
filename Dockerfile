FROM openjdk:11 as BUILD_IMAGE
USER root
WORKDIR /opt/example

COPY build/distributions/redis-0.1/ /opt/example/
RUN ls -la /opt/example/


FROM openjdk:11
USER root
WORKDIR /opt/example

COPY --from=BUILD_IMAGE /opt/example/ /opt/example/

ADD build/distributions/redis-0.1.jar /opt/example/lib/

