FROM openjdk:11-jre

ENV APP_PORT 8086
ENV WHEREABOUTS_VERSION "0.0.1-SNAPSHOT"
ENV WHEREABOUTS_JAR_URL http://repo.thehyve.nl/service/local/artifact/maven/redirect?r=snapshots&g=nl.thehyve&a=whereabouts&v=${WHEREABOUTS_VERSION}&p=jar

# non-overridable environment variables
ENV APP_USR whereabouts_usr
ENV APP_USR_HOME "/home/${APP_USR}"
ENV APP_CONFIG_FILE whereabouts.config.yml

COPY entrypoint.sh /entrypoint.sh

EXPOSE ${APP_PORT}

USER root

RUN useradd -m -d "${APP_USR_HOME}" -U ${APP_USR} && \
    sed -i 's/\r//' /entrypoint.sh && \
    chown "${APP_USR}" /entrypoint.sh && \
    chmod u+x /entrypoint.sh && \
    # download the jar
    curl -L "${WHEREABOUTS_JAR_URL}" -o "${APP_USR_HOME}/whereabouts-${WHEREABOUTS_VERSION}.jar"

USER ${APP_USR}
WORKDIR ${APP_USR_HOME}

ENTRYPOINT ["/entrypoint.sh"]
