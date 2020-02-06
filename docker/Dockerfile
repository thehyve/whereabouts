FROM openjdk:11-jre

ARG WHEREABOUTS_VERSION="0.0.1-SNAPSHOT"

ENV APP_VERSION ${WHEREABOUTS_VERSION}
ENV APP_PORT 8086

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
    if echo "${WHEREABOUTS_VERSION}" | grep '\.*-SNAPSHOT$' -; then \
      curl -f -L "https://repo.thehyve.nl/service/local/artifact/maven/redirect?r=snapshots&g=nl.thehyve&a=whereabouts&v=${WHEREABOUTS_VERSION}&p=jar" -o "${APP_USR_HOME}/whereabouts-${WHEREABOUTS_VERSION}.jar"; \
    else \
      curl -f -L "https://repo.thehyve.nl/service/local/artifact/maven/redirect?r=releases&g=nl.thehyve&a=whereabouts&v=${WHEREABOUTS_VERSION}&p=jar" -o "${APP_USR_HOME}/whereabouts-${WHEREABOUTS_VERSION}.jar"; \
    fi

USER ${APP_USR}
WORKDIR ${APP_USR_HOME}

ENTRYPOINT ["/entrypoint.sh"]
