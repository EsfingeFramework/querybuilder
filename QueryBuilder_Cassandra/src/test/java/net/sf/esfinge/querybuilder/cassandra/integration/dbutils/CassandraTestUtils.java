package net.sf.esfinge.querybuilder.cassandra.integration.dbutils;

import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;

public class CassandraTestUtils {

    public static void initDB() throws TTransportException, IOException, InterruptedException {
        initCassandraUnit();

        Session session = getSession();

        String query = "CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class':'SimpleStrategy','replication_factor':1};";

        session.execute(query);
        session.close();
    }

    public static void initCassandraUnit() throws TTransportException, IOException, InterruptedException {
        // Uncomment next line to use cassandra unit db instead of a local one
        // Need to use Java 1.8, this particular version of Cassandra Unit might not work with newer releases
        // EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
    }

    public static void createTables() {
        Session session = getSession();

        String query = "CREATE TYPE IF NOT EXISTS test.address (city text, state text);";

        session.execute(query);

        query = "CREATE TABLE IF NOT EXISTS test.Person(id int PRIMARY KEY, name text,lastname text, age int, address frozen<address>);";

        session.execute(query);
        session.close();
    }

    public static void populateTables() {
        Session session = getSession();

        String query = "BEGIN BATCH\n" +
                "        INSERT INTO test.Person(id, name, lastname, age, address) VALUES (1, 'Pedro', 'Silva', 20, {city: 'Juiz de Fora', state: 'MG'});\n" +
                "        INSERT INTO test.Person(id, name, lastname, age, address) VALUES (2, 'Maria', 'Ferreira', 23, {city: 'SJCampos', state: 'SP'});\n" +
                "        INSERT INTO test.Person(id, name, lastname, age, address) VALUES (3, 'Marcos', 'Silva', 50, {city: 'SJCampos', state: 'SP'});\n" +
                "        INSERT INTO test.Person(id, name, lastname, age, address) VALUES (4, 'Antonio', 'Marques', 33, {city: 'Juiz de Fora', state: 'MG'});\n" +
                "        INSERT INTO test.Person(id, name, lastname, age, address) VALUES (5, 'Silvia', 'Bressan', 11, {city: 'Juiz de Fora', state: 'MG'});\n" +
                "        APPLY BATCH";

        session.execute(query);


        session.close();
    }

    public static void cleanTables() {
        Session session = getSession();

        String query = "TRUNCATE test.Person";

        session.execute(query);

        session.close();
    }

    public static void dropDB() {
        Session session = getSession();

        String query = "DROP KEYSPACE IF EXISTS test";

        session.execute(query);
        session.close();
    }

    public static Session getSession() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();

        return client.getSession();
    }
}