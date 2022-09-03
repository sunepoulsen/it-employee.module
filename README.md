# IT Employee Module

IT Employee Module is the backend module of the IT Employee solution. 

## Purpose

The purpose is to provide one module for all data manipulating that is required by the frontend of the solution. 

## REST Endpoints

The module provide the following endpoints:

```
GET /actuator/health
```

Monitoring endpoint to see if the backend is running.

```
GET /swagger-ui.html
```

Online swagger documentation of all endpoints.

## Testing

The component and stress tests are excluded from the normal build cycle. As this is 
expected to be the normal cycle for daily development.

### Component tests

The component test is placed in the `it-employee-component-tests` subproject.

To run the component tests:

```
./gradlew -Pcomponent-tests :it-employee-component-tests:check
```

### Stress tests

The stress test is placed in the `it-employee-stress-tests` subproject.

#### JMeter

The stress test is generated with [JMeter](https://jmeter.apache.org/) version 5.4.3. Its required to have a version 
of jmeter installed to be able to run the stress tests.

#### Running

To run the stress tests:

```
./gradlew -Pstress-tests :it-employee-stress-tests:check
```

To run the stress tests with the local profile:

```
./gradlew -Pstress-tests :it-employee-stress-tests:check -Dstress.test.profile=local
```
