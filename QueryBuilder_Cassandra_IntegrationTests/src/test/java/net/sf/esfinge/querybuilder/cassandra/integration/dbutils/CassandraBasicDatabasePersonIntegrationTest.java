package net.sf.esfinge.querybuilder.cassandra.integration.dbutils;

import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

public class CassandraBasicDatabasePersonIntegrationTest {

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initDB();
        CassandraTestUtils.createTablesPerson();
    }
    @Before
    public void populateTables() {
        CassandraTestUtils.populateTablesPerson();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTablesPerson();
    }

    // DROPPING THE DB IN THE INTEGRATION TESTS CAN LEAD TO UNEXPECTED ERRORS
    /*@AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }*/

}
