package org.esfinge.querybuilder.jdbc.testresources;

import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.After;

public class DataBaseCommunicator {

	private JdbcDatabaseTester jdt;

	public void initializeDatabase(String filename)
			throws ClassNotFoundException, Exception {
		jdt = new JdbcDatabaseTester(
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:file:./db/esfingejdbc;ifexists=true;shutdown=true;",
				"sa", "");
		DataFileLoader loader = new FlatXmlDataFileLoader();
		IDataSet dataSet = loader.load(filename);
		jdt.setDataSet(dataSet);
		jdt.onSetup();
	}

	@After
	public void cleanDatabase() throws Exception {
		jdt.onTearDown();
	}

	public void compareTables(String expectedDatasetFileName,
			String... tableNames) throws Exception {		
		IDataSet databaseDataSet =  new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:./db/esfingejdbc;ifexists=true;shutdown=true;", "sa", "").getConnection().createDataSet();		
		IDataSet expectedDataSet = new FlatXmlDataFileLoader()
				.load(expectedDatasetFileName);
		for (String tableName : tableNames) {
			ITable actualTable = databaseDataSet.getTable(tableName);
			ITable expectedTable = expectedDataSet.getTable(tableName);
			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

}
