package net.sf.esfinge.querybuilder.util;

import java.sql.Connection;
import java.sql.DriverManager;

import net.sf.esfinge.querybuilder.jdbc.DatabaseConnectionProvider;

public class DatabaseConnection implements DatabaseConnectionProvider {

	public Connection getConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			return DriverManager.getConnection("jdbc:hsqldb:file:./db/example-db;shutdown=true", "sa", "");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return null;
	}

}
