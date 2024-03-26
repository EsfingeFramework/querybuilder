package net.sf.esfinge.querybuilder.jdbc;

import java.sql.Connection;

public interface DatabaseConnectionProvider {

    Connection getConnection();

}
