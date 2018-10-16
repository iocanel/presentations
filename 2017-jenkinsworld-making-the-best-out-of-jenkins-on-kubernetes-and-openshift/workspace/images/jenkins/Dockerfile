From jenkins/jenkins:2.75-alpine

# Disable the setup wizard
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

# Install the docker binaries
# This is only needed to demonstrate docker-workflow
USER root
RUN echo http://nl.alpinelinux.org/alpine/edge/community >> /etc/apk/repositories && \
    apk add --no-cache docker
USER jenkins

# Copy plugins manifest
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

# Copy binary plugins (e.g. custom builds like kubernetes-plugin, kubernetes-pipeline-plugin)
COPY plugins/*.hpi /usr/share/jenkins/ref/plugins/

# Copy groovy scripts
#COPY config/*.groovy /usr/share/jenkins/ref/init.groovy.d/

# Copy configuration (e.g. Kubernetes Cloud settings)
COPY config/*.xml /usr/share/jenkins/ref/

# Copy jobs (it currently contains just an empty pipeline job)
COPY jobs/dev /usr/share/jenkins/ref/jobs/dev

# Local Variables:
# docker-image-name: "iocanel/jenkins:latest"
# End:
