package net.sf.esfinge.querybuilder.cassandra.integration.dbutils;

import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

public class CassandraBasicDatabaseTest {

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initDB();
        CassandraTestUtils.createTables();
    }


    // TODO: DROPPING THE DB IN THE INTEGRATION TESTS CAN LEAD TO UNEXPECTED ERRORS
    /*@AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }*/

    @Before
    public void populateTables() {
        CassandraTestUtils.populateTables();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTables();
    }
}
