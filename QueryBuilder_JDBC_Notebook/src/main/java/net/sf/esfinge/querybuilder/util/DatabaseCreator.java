package net.sf.esfinge.querybuilder.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.esfinge.querybuilder.jdbc.DatabaseConnectionProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class DatabaseCreator {

	public static void main(String[] args) {

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

			query = "drop table contact";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the CONTACT does not exist, so will be created");
			;
		}

		try {

			query = "drop table person";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the table PERSON does not exist, so will be created");
			;
		}

		try {

			query = "drop table address";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the table ADDRESS does not exist, so will be created");
			;
		}

		try {

			query = "CREATE MEMORY TABLE PUBLIC.ADDRESS(ADDRESSID INTEGER NOT NULL PRIMARY KEY,ADDRESSCITY VARCHAR(100),ADDRESSSTATE VARCHAR(2),ADDRESSZIPCODE VARCHAR(9),ADDRESSSTREET VARCHAR(100),ADDRESSSTREETNUMBER VARCHAR(10),ADDRESSSTREETCOMPLEMENT VARCHAR(50))";

			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {

			query = "CREATE MEMORY TABLE PUBLIC.PERSON(PERSONID INTEGER NOT NULL PRIMARY KEY,PERSONNAME VARCHAR(100),PERSONSURNAME VARCHAR(100),PERSONBIRTHDATE VARCHAR(10))";

			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {

			query = "CREATE MEMORY TABLE PUBLIC.CONTACT(CONTACTID INTEGER NOT NULL PRIMARY KEY,CONTACTPERSONID INTEGER,CONTACTADDRESSID INTEGER,CONTACTEMAIL VARCHAR(100),CONTACTPHONENUMBER VARCHAR(15), CONTACTCELLNUMBER VARCHAR(15),CONTACTTYPE VARCHAR(50),CONSTRAINT FK8E48877550774A0 FOREIGN KEY(CONTACTPERSONID) REFERENCES PUBLIC.PERSON(PERSONID),CONSTRAINT FK8E48877550774A1 FOREIGN KEY(CONTACTADDRESSID) REFERENCES PUBLIC.ADDRESS(ADDRESSID))";

			stm.executeUpdate(query);

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
