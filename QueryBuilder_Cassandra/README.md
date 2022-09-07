# Querybuilder Cassandra

This project extends the [Esfinge Querybuilder](http://esfinge.sourceforge.net/Query%20Builder.html) framework whith an 
integration for the `NoSQL` database Apache Cassandra. Cassandra is an open source NoSQL distributed database
trusted by thousands of companies for scalability and high availability without compromising performance

# Table of contents

* [Prerequisites](#prerequisites)
* [How to configure the project ](#how-to-configure-the-project-on-intellij-idea)
* [How to use the Querybuilder](#how-to-use-the-querybuilder)
    * [Project configuration](#project-configuration)
    * [Depencencies](#dependencies)
* [Framework limitations](#framework-limitations)
* [Author](#author)
* [Notes](#notes)

# Prerequisites

* Java 1.8 or higher.
* Maven

# How to configure the project (on IntelliJ Idea)

* Open the project in `IntelliJ`
* Load `Maven script` if prompted by the IDE
* Run the tests with `mvn test`. Please note that there is another project in this repository called `QueryBuilder_Cassandra_IntegrationTests`, which contains the integration tests performed with a packaged version of `QueryBuilder Cassandra`. 

# How to use the QueryBuilder

For an overall view of the `Esfinge Querybuilder` utilization you can view the
general [documentation](../documentation/README.md) on the framework. The following is a specific guide for using the
Cassandra plugin along with the framework.
Please keep in mind that for this configuration guide we suppose that you will be using Maven, but you are free to use
any other method to manage dependencies.

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

* In the `META-INF/services/` folder you need to create the following three files:
    * net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider
    * net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity
* In the `META-INF/` folder you can also add an optional `config.json` file, see [framework limitations](#framework-limitations) 
for more information. 
* Now you need to provide the concrete implementations of these two intefaces, namely: `CassandraSessionProvider`
  , `CassandraEntity`.
  For example for `CassandraSessionProvider`:

```Java
public class MySessionProvider implements CassandraSessionProvider {

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
}
```

For the classes implementing the `CassandraEntity` please keep in mind that you have to use the `@Table`
and `@PartitionKey` annotations of the `com.datastax.driver.mapping.annotations` library, which is used by this
framework to do the object mapping.

* In the files contained in the `META-INF/services` folder you need to include the full path to your implementation of
  the
  specific interface, for example in the `net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider`, supposing you
  named the implementation as `MySessionProvider` in the package `org.example` you will include the
  path `org.example.MySessionProvider`.
* Create an interface that extends the `net.sf.esfinge.querybuilder.Repository` interface, by parametrizing it with an
  entity class, which should implement
  the `CassandraEntity` interface, for example:

```Java
public interface PersonDao extends Repository<Person> {

    List<Person> getPerson();

    Person getPersonById(Integer id);

}
```

Please keep in mind that you are not forced to implement new custom methods like in the previous example, the
basic `CRUD` operations, for example `save` and `delete` are available by default.

* Here is how to create the instance of the class that actually implements this interface:

```Java
PersonDao dao=QueryBuilder.create(PersonDao.class);
```

* Now you are ready to go and the framework should already work. For example you could call the
  method `dao.getPersonById(1)` and the framework will manage connecting to the database,
  translating the method into a query and retrieving the results as a `Person` class.
* Please keep in mind that the database does not automatically create the `Keyspace` and the `Tables` for storing the
  data, this is left to the user, which can also use the `KeyspaceRepository` class of the framework, which provides
  utility methods for handling keyspaces and tables.

### Managing entities with custom attributes

If you are using an entity with a custom attribute, you need to use the datastax `@UDT` annotation
when defining that custom attribute, for example for:

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
    private Address address; // CUSTOM ATTRIBUTE

    // getters and setters omitted...
}
```

You need to define the `Address` class by providing the annotation:

```Java

@UDT(keyspace = "test", name = "address")
public class Address implements CassandraEntity {
    private String city;
    private String state;

    // getters and setters omitted...
}
```

In addition, when you are defining the table in the cassandra database,
you need first to create the type and to provide the `frozen` attribute for
the custom type in the table.
A possible definition of table and type for the
classes above would be:

```SQL
CREATE TYPE test.address (city text, state text);
```

And for the table:

```SQL
CREATE TABLE test.Person
(
    id int PRIMARY KEY,
    name text,
    address frozen<address>
);
```

## Dependencies

* You need to add the following dependencies either in your `pom.xml` file or as local `.jar` files, here is a list of the `Maven` dependencies needed:

```xml

<dependencies>

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
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.6.4</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.13.3</version>
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

</dependencies>
```

* You will notice that the `querybuilder-cassandra` dependency is the only local one. This i because the software has
  not been released in the maven repository, but needs to be added as local `.jar` file. In order to create the `.jar`
  file for use you can open this project with maven and run `mvn package` , the `.jar` file will be created in
  the `/target` folder.

# Framework limitations

The cassandra database works similar to a relational database, but since its main purpose is to handle huge amounts of
data and provide reliability and scaling mechanisms there are some differences in which operations are allowed or not.
The following are the main limitations of cassandra with respect to a relational database:

* Join Queries are not supported
* Queries with `OR` are not supported
* Ordering is not supported
* `LIKE`, `STARTS`, `ENDS` and `CONTAINS` operators are not supported.
* Comparing to null is not supported

All these limitations have been overcome by the framework by adding a layer at the application level. Please keep in mind
that joins are only limited to a depth of 1, for this reason a custom attribute must not contain other custom
attributes itself.

In the `META-INF/` folder you can also add an optional `config.json` file, where you can configure the maximum number of 
entities that can be ordered from a result and the maximum depth of secondary queries. Secondary queries are used to mimic
the behaviour of the `OR` operator, this limit basically defines the maximum number of `OR` connectors in the query, here is an 
example fo the configuration file:

```Json
{
  "orderingLimit": 10,
  "secondaryQueryLimit": 2
}
```

If this file is omitted, the configuration will automatically default to: 
```Json
{
  "orderingLimit": 1000,
  "secondaryQueryLimit": 3
}
```

# Author

**Name**: Samuel Dalvai <br>
**Email**: samdalvai@unibz.it <br>

# Notes

This project has been developed at the Free University of Bolzano, as argument for the final thesis
of a BSc in Computer Science, with the supervision of Professor Martins Guerra Eduardo, which is also the author of 
the `Esfinge Querybuilde Framework`. Please visit [Esfinge Querybuilder](http://esfinge.sourceforge.net/Query%20Builder.html)
for more information on the framework.