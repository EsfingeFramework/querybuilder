# Querybuilder Cassandra - Integration tests
This project is used to perform various integrations tests on the functionalities of the Cassandra query builder plugin for the `Esfinge Framework`.
The projects `pom.xml` relies on the local dependency of the `.jar` file of the plugin. In order to update the jar, you need to substitute/update the one which is already present in the `/resources` folder in 
the root of the project. If the version of the jar has changed, for example from `querybuilder-cassandra-1.0.0.jar` to `querybuilder-cassandra-1.1.0.jar` you need also to update the corresponidn entry 
in the `pom.xml` file.

# Prerequisites
* Java 1.8 or higher. If you want to use `Cassandra Unit` instead of a local version of the cassandra Database, you should not use java versions higher than 1.8, otherwise the component might not work properly.
* Maven
* Docker

# How to configure (on IntelliJ Idea)

* Open the project in `IntelliJ`
* Load `Maven script` if prompted by the IDE
* Run `docker-compose up -d` to run the Cassandra database or use `Cassandra Unit` by uncommenting the line in the method `initCassandaUnit` of the class `net.sfesfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils`.
* Run the tests with `mvn test`
