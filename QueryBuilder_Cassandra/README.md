# Querybuilder Cassandra
This project extends the [Esfinge Querybuilder](http://esfinge.sourceforge.net/Query%20Builder.html) whit a plugin for the integration with the `NoSQL` distributed database Cassandra.

# Table of contents
* [Prerequisites](#prerequisites)
* [How to configure the project ](#how-to-configure-the-project-on-intellij-idea)
* [How to use the Querybuilder](#how-to-use-the-querybuilder)

# Prerequisites
* Java 1.8 or higher. If you want to use `Cassandra Unit` instead of a local version of the cassandra Database, you should not use java versions higher than 1.8, otherwise the component might not work properly.
* Maven
* Docker

# How to configure the project (on IntelliJ Idea)

* Open the project in `IntelliJ`
* Load `Maven script` if prompted by the IDE
* Run `docker-compose up -d` to run the Cassandra database or use `Cassandra Unit` by uncommenting the line in the method `initCassandaUnit` of the class `net.sfesfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils`.
* Run the tests with `mvn test`

# How to use the QueryBuilder
For an overall view of the `Esfinge Querybuilder` utilization you can view the general [documentation](../documentation/README.md) on the framework. The following is a specific guide for using the Cassandra plugin along with the framework. 
Please keep in mind that for this configuration guide we suppose that you will be using Maven, but you are free to use any other method to manage dependencies.

## Project configuration
* Create an empty project with the following structure:

```markdown
└───src
    └───main
        ├───java
        └───resources
            └───META-INF
                └───services
```
* In the `META-INF` folder you need to create the following three files:
  * net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider
  * net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity
  * net.sf.esfinge.querybuilder.Repository
* Now you need to provide the concrete implementations of two three intefaces, namely: `CassandraSessionProvider`, `CassandraEntity`.
For example for `CassandraSessionProvider`:
```Java
public class MySessionProvider  implements CassandraSessionProvider {

    @Override
    public Session getSession() {
        // your implementation...
        return null;
    }

}
```
And for `CassandraEntity`:
```Java
@Table(keyspace = "test", name = "person",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Person implements CassandraEntity {

    @PartitionKey
    private Integer id;
    private String name;
    
    // getters and setters omitted...
```
For the classes implementing the `CassandraEntity` please keep in mind that you have to use the `@Table` and `@PartitionKey` annotations of the `com.datastax.driver.mapping.annotations` library, which is used by this framework to do the object mapping.   

* In the files contained in the `META-INF` folder you need to include the full path to your implementation of the specific interface, for example in the `net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider`, supposing you named the implementation as `MySessionProvider` in the package `org.example` you will include the path `org.example.MySessionProvider`. For the `Repository` you just need to include the following path, since this interface is already implemented by the framework: `net.sf.esfinge.querybuilder.cassandra.CassandraRepository`  
* Create an interface that extends the `Repository`, by parametrizing it with an entity class, which should implement the `CassandraEntity` interface, for example:  

```Java
public interface CassandraSimpleTestQuery extends Repository<Person> {

  List<Person> getPerson();

  Person getPersonById(Integer id);

}

```
Please keep in mind that you are not forced to implement new custom methods like in the previous example, the basic `CRUD` operations, for example `save` and `delete` are available by default.  

* Now you are ready to go and the framework should already work. For example you could call the method `CassandraSimpleTestQuery.getPersonById(1)` and the framework will manage connecting to the database, translating the method into a query and retrieving the results as a `Person` class.
* Please keep in mind that the database does not automatically create the `Keyspace` and the `Tables` for storing the data, this is left to the user, which can also use the `KeyspaceRepository` class of the framework, which provides utility methods for handling keyspaces and tables.

## Dependencies
* You need to add the following dependencies either in your `pom.xml` file or as local `.jar` files, for the purpose of this guide we list the dependencies in the `Maven` style:
```xml
<!-- https://mvnrepository.com/artifact/com.datastax.cassandra/cassandra-driver-core -->
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-core</artifactId>
            <version>3.1.0</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.guava</groupId>
                    <artifactId>guava</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.datastax.cassandra/cassandra-driver-mapping -->
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-mapping</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.6.1</version>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.6.4</version>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/net.sf.esfinge/querybuilder-core -->
        <dependency>
            <groupId>net.sf.esfinge</groupId>
            <artifactId>querybuilder-core</artifactId>
            <version>1.7.0</version>
        </dependency>

        <!-- this is a local dependency -->
        <dependency>
            <groupId>net.sf.esfinge</groupId>
            <artifactId>querybuilder-cassandra</artifactId>
            <version>1.0.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/resources/querybuilder-cassandra-1.0.0.jar</systemPath>
        </dependency>

```
* You will notice that the `querybuilder-cassandra` dependency is the only local one. This i because the software has not been released in the maven repository, but needs to be added as local `.jar` file. In order to create the `.jar` file for use you can open this project with maven and run `mvn package` , the `.jar` file will be created in the `/target` folder. 

## Framework limitations
The cassandra database works similar to a relational database, but since its main purpose is to handle huge amounts of data and provide reliability and scaling mechanisms there are some differences in which operations are allowed or not.
CONTINUES...