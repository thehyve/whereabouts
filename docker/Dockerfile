FROM openjdk:8-jre-alpine

ENV APP_PORT 8086
ENV WHEREABOUTS_VERSION "0.0.1-SNAPSHOT"
# to be removed and replaced with WHEREABOUTS_VERSION after first release
ENV WHEREABOUTS_SNAPSHOT_VERSION "0.0.1-20190606.073652-5"
ENV WHEREABOUTS_JAR_URL https://repo.thehyve.nl/content/repositories/snapshots/nl/thehyve/whereabouts/${WHEREABOUTS_VERSION}/whereabouts-${WHEREABOUTS_SNAPSHOT_VERSION}.jar

# non-overridable environment variables
ENV APP_USR whereabouts_usr
ENV APP_USR_HOME "/home/${APP_USR}"
ENV APP_CONFIG_FILE whereabouts.config.yml

COPY entrypoint.sh /entrypoint.sh

EXPOSE ${APP_PORT}

USER root

RUN adduser -h "${APP_USR_HOME}" -D ${APP_USR} && \
    sed -i 's/\r//' /entrypoint.sh && \
    chown "${APP_USR}" /entrypoint.sh && \
    chmod u+x /entrypoint.sh

# download the jar
RUN wget "${WHEREABOUTS_JAR_URL}" -O "${APP_USR_HOME}/whereabouts-${WHEREABOUTS_VERSION}.jar"

USER ${APP_USR}
WORKDIR ${APP_USR_HOME}

ENTRYPOINT ["/entrypoint.sh"]