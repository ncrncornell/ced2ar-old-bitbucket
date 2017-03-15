
# Building CED2AR

* Dev builds can be executed by using `mvn install`.
* Production builds need the `build` profile: `mvn install -Pbuild`. This will apply a variety of production-level settings, such as full optimization of generated JavaScript code.
* IntelliJ Spring Boot application runner: you'll want to add the following tasks before the 'Before Launch' part of the configuration, after 'Build'

    1. `antrun:run` (for windows only); generates JavaScript
    2. `process-test-resources`; places JavaScript files in appropriate directory
    3. If the IntelliJ build step tries to compile code meant for sbt, you can right click on the build output log and select something like "exclude from compile" or "exclude from build".


# Configuring CED2AR

Options should be chosen by by editing `ed2ar3-site/WEB-INF/classes/application-default.properties`. Common settings to override:

* `spring.datasource.url` = your db connection string, e.g., jdbc:postgresql://ip/db
*  `spring.datasource.username` = your db user name
*  `spring.datasource.password` = your db password
*  `ced2ar.uploadDir` = An absolute path to the folder where xml files are uploaded by users. 