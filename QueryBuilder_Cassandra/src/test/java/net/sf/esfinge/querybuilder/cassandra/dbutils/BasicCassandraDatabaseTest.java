package net.sf.esfinge.querybuilder.cassandra.dbutils;

import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

public class BasicCassandraDatabaseTest {

    protected TestCassandraSessionProvider provider;

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        // Uncomment next line to use cassandra unit db instead of a local one
        // EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
        CassandraTestUtils.initDB();
    }

    @Before
    public void populateTables() {
        CassandraTestUtils.populateTables();

        provider = new TestCassandraSessionProvider();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTables();
    }

    @AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }
}
