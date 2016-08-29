package org.esfinge.querybuilder.jpa1.testresources;

import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.After;

public class QueryBuilderDatabaseTest {

	protected JdbcDatabaseTester jdt;

	protected void initializeDatabase(String filename)
			throws ClassNotFoundException, Exception {
				TestEntityManagerProvider provider =  new TestEntityManagerProvider();
				provider.getEntityManagerFactory();
				
				jdt = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:./db/example-db", "sa", "");
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
		IDataSet databaseDataSet = jdt.getConnection().createDataSet();
		IDataSet expectedDataSet = new FlatXmlDataFileLoader().load(expectedDatasetFileName);
		for (String tableName : tableNames) {
			ITable actualTable = databaseDataSet.getTable(tableName);
			ITable expectedTable = expectedDataSet.getTable(tableName);
			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

}