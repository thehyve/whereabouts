# whereabouts


[![Build Status](https://travis-ci.org/thehyve/whereabouts.svg?branch=master)](https://travis-ci.org/thehyve/whereabouts/branches)

Data warehouse Inventory Management System that maintains a catalogue of data warehouse instances, 
with information about how they were created (to enable restoring a similar instance) 
and how to connect to them (required for the data loading and slicing services).

## Run, test, deploy

Make sure you have Maven installed.

```bash
# run the application
mvn spring-boot:run
```
There should now be an application running at [http://localhost:8080/](http://localhost:8080/).


### Package
```bash
# create a war package
mvn package
```
There should now be a `.war`-file in `target/whereabouts-<version>.jar`.
```bash
# run the packaged application
java -jar target/whereabouts-<version>.jar
```


### Tests

Run all tests. The test will use the configuration from `./src/test/resource/application.properties` file.

```bash
mvn test
```

## Usage


### API calls:

- GET `/instances`
   - Gets a list of all instances.
- GET `/instances/<id>`
   - Gets an instance with the given `<id>`.
- POST `/instances`
   - Adds a new instance with the properties provided in the body.
   Request body:
   ```
   {
             "address": "string",
             "sourceQuery": "string"
   }
   ```
- PUT `/instances/<id>`
   - Updates the instance with given `<id>` with the values in the body.
      Request body:
      ```
      {
             "address": "string",
             "sourceQuery": "string"
      }
      ```

License
-------

Copyright (c) 2019 The Hyve B.V.

The Data warehouse Inventory Management System is licensed under the MIT License. See the file `<LICENSE>`_.