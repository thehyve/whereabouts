[![Docker Build Status](https://img.shields.io/docker/pulls/thehyve/whereabouts.svg)](https://hub.docker.com/r/thehyve/whereabouts)

Docker image definitions and docker-compose profiles to run whereabouts and its database.
`whereabouts` runs the application server through OpenJDK, and `whereabouts-database` its database through PostgreSQL.

## Build
Build the image with:
```bash
docker build -t thehyve/whereabouts . --no-cache
```

## Usage
Run the full stack with:
```bash
export KEYCLOAK_SERVER_URL=https://keycloak.example.com
export KEYCLOAK_REALM=dev
export KEYCLOAK_CLIENT_ID=whereabouts
docker-compose -f docker-compose.yml up
```

Those command will build or download the images, and run the containers.
