FROM openjdk:8-jdk-alpine

ENV HOME /home/jenkins

ARG VERSION=3.10
ARG AGENT_WORKDIR=/home/jenkins/agent

RUN apk add --update --no-cache curl bash git openssh-client openssl \
  && curl --create-dirs -sSLo /usr/share/jenkins/slave.jar https://repo.jenkins-ci.org/public/org/jenkins-ci/main/remoting/${VERSION}/remoting-${VERSION}.jar \
  && chmod 755 /usr/share/jenkins \
  && chmod 644 /usr/share/jenkins/slave.jar \
  && apk del curl

ENV AGENT_WORKDIR=${AGENT_WORKDIR}
RUN mkdir -p /home/jenkins/.jenkins && mkdir -p ${AGENT_WORKDIR}
COPY jenkins-agent /usr/local/bin/jenkins-agent

VOLUME /home/jenkins/.jenkins
VOLUME ${AGENT_WORKDIR}
WORKDIR /home/jenkins
