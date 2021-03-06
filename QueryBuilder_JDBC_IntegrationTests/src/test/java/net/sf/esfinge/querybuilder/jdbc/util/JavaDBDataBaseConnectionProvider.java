package net.sf.esfinge.querybuilder.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;

import net.sf.esfinge.querybuilder.jdbc.DatabaseConnectionProvider;

public class JavaDBDataBaseConnectionProvider implements DatabaseConnectionProvider {

	public Connection getConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			return DriverManager.getConnection("jdbc:hsqldb:file:./db/example-db", "sa", "");
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
