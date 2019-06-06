# whereabouts

[![Build Status](https://travis-ci.org/thehyve/whereabouts.svg?branch=master)](https://travis-ci.org/thehyve/whereabouts/branches)
[![codecov](https://codecov.io/gh/thehyve/whereabouts/branch/master/graph/badge.svg)](https://codecov.io/gh/thehyve/whereabouts)

Data warehouse Inventory Management System that maintains a catalogue of data warehouse instances, 
with information about how they were created (to enable restoring a similar instance) 
and how to connect to them (required for the data loading and slicing services).


## Run, test, deploy

Run the application and its database using [docker](./docker/README.md) or follow the instruction below.

### Configure PostgreSQL database
```bash
sudo -u postgres psql
```

```bash
create user wa with password 'wa';
create database whereabouts;
grant all privileges on database whereabouts to wa;
```

Required tables will be created on application startup using Liquibase. 
This behaviour can be disabled by this setting:
```
spring.liquibase.enabled=false
```

### Configure Keycloak
Whereabouts uses Keycloak as an identity provider.
The following settings need to be configured before running the application.

| yaml key | description |
|-----------|-------------|
| `keycloak.auth-server-url` | keycloak url that used. e.g. `https://keycloak.example.com/auth` |
| `keycloak.realm` | keycloak realm. |
| `keycloak.resource` | keycloak client id. |


There are 3 Keycloak client roles that need to be added for users to enable the access to all endpoints:
- read-instances,
- create-instances,
- change-instances;

See [swagger documentation](/src/main/resources/public/swagger.yaml) for more details.

### Run

Make sure you have Maven installed.

```bash
# run the application
mvn spring-boot:run
```
There should now be an application running at [http://localhost:8086/](http://localhost:8086/).


### Package
```bash
# create a jar package
mvn clean package
```
There should now be a `.jar`-file in `target/whereabouts-<version>.jar`.
```bash
# run the packaged application
java -jar target/whereabouts-<version>.jar
```


### Tests

Run all tests. The test will use the configuration from `./src/test/resource/application.yml` file.

```bash
mvn test
```

License
-------

Copyright (c) 2019 The Hyve B.V.

The Data warehouse Inventory Management System is licensed under the MIT License. See the file [LICENSE](LICENSE).
