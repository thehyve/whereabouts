#!/usr/bin/env bash

here=$(dirname "${0}")
WHEREABOUTS_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)

docker build --build-arg "WHEREABOUTS_VERSION=${WHEREABOUTS_VERSION}" -t "thehyve/whereabouts:${WHEREABOUTS_VERSION}" "${here}/../docker" && \
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD" && \
docker push "thehyve/whereabouts:${WHEREABOUTS_VERSION}"
