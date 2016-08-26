package org.esfinge.querybuilder.jdbc.testresources;

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
			System.out.println("Operation: drop table PERSON");
			query = "drop table person";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the table PERSON does not exist, so will be created");
			;
		}

		try {
			System.out.println("Operation: drop table ADDRESS");
			query = "drop table address";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the table ADDRESS does not exist, so will be created");
			;
		}

		try {
			System.out.println("Operation: drop table WORKER");
			query = "drop table worker";
			stm.executeUpdate(query);

		} catch (SQLException e) {
			System.out
					.println("Operation: the table WORKER does not exist, so will be created");
			;
		}

		try {
			System.out.println("Operation: create table ADDRESS");
			query = "CREATE MEMORY TABLE PUBLIC.ADDRESS(ID INTEGER NOT NULL PRIMARY KEY,CITY VARCHAR(255),STATE VARCHAR(255))";

			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: create table PERSON");
			query = "CREATE MEMORY TABLE PUBLIC.PERSON(ID INTEGER NOT NULL PRIMARY KEY,AGE INTEGER,LASTNAME VARCHAR(255),NAME VARCHAR(255),ADDRESS_ID INTEGER,CONSTRAINT FK8E48877550774A0 FOREIGN KEY(ADDRESS_ID) REFERENCES PUBLIC.ADDRESS(ID))";

			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: create table WORKER");
			query = "CREATE MEMORY TABLE PUBLIC.WORKER(ID INTEGER NOT NULL PRIMARY KEY,AGE INTEGER,LASTNAME VARCHAR(255),NAME VARCHAR(255))";

			stm.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert address(1)");
			query = "insert into address (id,city,state)values(1,'SJCampos','SP')";

			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert address(2)");
			query = "insert into address (id,city,state)values(2,'Campinas','SP')";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert address(3)");
			query = "insert into address (id,city,state) values(3,'Juiz de Fora','MG')";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert person(1)");
			query = "insert into person(id,name,lastname,age,address_id) values (1,'Pedro','Silva',15,3)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert person(2)");
			query = "insert into person(id,name,lastname,age,address_id) values (2,'Maria',null,17,1)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert person(3)");
			query = "insert into person(id,name,lastname,age,address_id) values (3,'Marcos','Silva',70,2)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert person(4)");
			query = "insert into person(id,name,lastname,age,address_id) values (4,null,'Marques',10,3)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert person(5)");
			query = "insert into person(id,name,lastname,age,address_id) values (5,'Silvia','Bressan',90,3)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert worker(1)");
			query = "insert into worker(id,name,lastname,age) values (1,'Pedro','Silva',15)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert worker(2)");
			query = "insert into worker(id,name,lastname,age) values (2,'Maria',null,17)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert worker(3)");
			query = "insert into worker(id,name,lastname,age) values (3,'Marcos','Silva',70)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert worker(4)");
			query = "insert into worker(id,name,lastname,age) values (4,null,'Marques',10)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Operation: insert worker(5)");
			query = "insert into worker(id,name,lastname,age) values (5,'Silvia','Bressan',90)";
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("SHUTDOWN...");

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
