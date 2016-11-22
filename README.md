
# Building CED2AR

* Dev builds can be executed by using `mvn install`.
* Production builds need the `build` profile: `mvn install -Pbuild`. This will apply a variety of production-level settings, such as full optimization of generated JavaScript code.
* IntelliJ Spring Boot application runner: you'll want to add the following tasks before the 'Before Launch' part of the configuration, after 'Build'

    1. `antrun:run` (for windows only); generates JavaScript
    2. `process-test-resources`; places JavaScript files in appropriate directory
