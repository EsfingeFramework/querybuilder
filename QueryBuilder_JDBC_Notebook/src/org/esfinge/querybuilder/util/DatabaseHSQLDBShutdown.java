package org.esfinge.querybuilder.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.esfinge.querybuilder.jdbc.DatabaseConnectionProvider;
import org.esfinge.querybuilder.utils.ServiceLocator;

public class DatabaseHSQLDBShutdown {

	public static void executeHSQLDBShutdown() {

		Connection c;
		String query = null;

		DatabaseConnectionProvider dcp = ServiceLocator
				.getServiceImplementation(DatabaseConnectionProvider.class);
		c = dcp.getConnection();

		Statement stm = null;

		try {
			stm = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {

			query = "SHUTDOWN";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (stm != null) {
			try {
				stm.close();
				stm = null;
			} catch (SQLException sqlerr) {
			}
		}

		if (c != null) {

			try {
				c.close();
			} catch (SQLException sqlerr) {
			}
		}

	}
}
