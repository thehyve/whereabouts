#!/bin/sh
set -e

cat > "${APP_CONFIG_FILE}" <<EndOfMessage
---
keycloak:
  auth-server-url: ${KEYCLOAK_SERVER_URL}/auth
  realm: ${KEYCLOAK_REALM}
  resource: ${KEYCLOAK_CLIENT_ID}

spring:
  datasource:
    platform: postgresql
    driver-class-name: org.postgresql.Driver
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    hikari:
      jdbc-url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}

EndOfMessage

exec java -jar -server \
        "-Djava.awt.headless=true" \
        "-Dmail.mime.decodeparameters=true" \
        "-Djava.security.egd=file:///dev/urandom" \
        "-Dspring.config.location=classpath:/config/application.yml,${APP_CONFIG_FILE}" \
    "whereabouts-${APP_VERSION}.jar"
