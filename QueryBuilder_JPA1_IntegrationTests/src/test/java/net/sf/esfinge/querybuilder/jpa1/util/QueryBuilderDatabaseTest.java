package net.sf.esfinge.querybuilder.jpa1.util;

import net.sf.esfinge.querybuilder.jpa1.testresources.TestEntityManagerProvider;

import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.After;

public class QueryBuilderDatabaseTest {

	protected JdbcDatabaseTester jdt;

	protected void initializeDatabase(String filename) throws ClassNotFoundException, Exception {
		jdt = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:./db/example-db", "sa", "");

		TestEntityManagerProvider provider = new TestEntityManagerProvider();
		provider.getEntityManagerFactory();
		
		DatabaseConfig config = jdt.getConnection().getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		
		DataFileLoader loader = new FlatXmlDataFileLoader();
		IDataSet dataSet = loader.load(filename);
		jdt.setDataSet(dataSet);
		jdt.onSetup();
	}

	@After
	public void cleanDatabase() throws Exception {
		jdt.onTearDown();
	}

	protected void compareTables(String expectedDatasetFileName, String... tableNames) throws Exception {
		IDatabaseConnection connection = jdt.getConnection();
		
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		
		IDataSet databaseDataSet = connection.createDataSet();
		IDataSet expectedDataSet = new FlatXmlDataFileLoader().load(expectedDatasetFileName);
		for (String tableName : tableNames) {
			ITable actualTable = databaseDataSet.getTable(tableName);
			ITable expectedTable = expectedDataSet.getTable(tableName);
			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

}