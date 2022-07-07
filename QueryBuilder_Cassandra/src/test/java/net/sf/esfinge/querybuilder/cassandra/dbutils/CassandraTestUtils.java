package net.sf.esfinge.querybuilder.cassandra.dbutils;


import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.ReplicationStrategy;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

public class CassandraTestUtils {

    private static final String TABLE_NAME = "person";
    private static final String KEYSPACE_NAME = "test";

    public static void initDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();
        KeyspaceRepository schemaRepository = new KeyspaceRepository(session);

        schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 1);

        createTables(session);

        client.close();
    }

    public static void createTables(Session session) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(KEYSPACE_NAME + "." + TABLE_NAME)
                .append("(")
                .append("id int PRIMARY KEY, ")
                .append("name text,")
                .append("lastname text,")
                .append("age int);");

        final String query = sb.toString();

        session.execute(query);
    }

    public static void insertPerson(Person person, Session session) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(KEYSPACE_NAME + "." + TABLE_NAME)
                .append("(id, name, lastname, age) ")
                .append("VALUES (")
                .append(person.getId())
                .append(", '")
                .append(person.getName()).append("', '")
                .append(person.getLastName()).append("', ")
                .append(person.getAge()).append(");");

        final String query = sb.toString();

        session.execute(query);
    }

    public static void populateTables() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        Person person1 = new Person();
        person1.setId(1);
        person1.setName("Homer");
        person1.setLastName("Simpson");
        person1.setAge(48);

        Person person2 = new Person();
        person2.setId(2);
        person2.setName("Max");
        person2.setLastName("Power");
        person2.setAge(50);

        insertPerson(person1, session);
        insertPerson(person2, session);

        client.close();
    }

    public static void cleanTables() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();

        StringBuilder sb = new StringBuilder("TRUNCATE ")
                        .append(KEYSPACE_NAME + "." + TABLE_NAME);

        final String query = sb.toString();

        session.execute(query);
    }


    public static void dropDB() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();
        Session session = client.getSession();
        KeyspaceRepository schemaRepository = new KeyspaceRepository(session);

        schemaRepository.deleteKeyspace(KEYSPACE_NAME);
        client.close();
    }
}
