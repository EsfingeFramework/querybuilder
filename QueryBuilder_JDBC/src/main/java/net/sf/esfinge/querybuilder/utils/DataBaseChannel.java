package net.sf.esfinge.querybuilder.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseChannel {

    private Connection connection;

    public DataBaseChannel(Connection con) {
        setConnection(con);
    }

    public void setConnection(Connection con) {
        this.connection = con;
    }

    public boolean checkConnection() {
        try {
            return (connection == null || !connection.isClosed() || connection
                    .isValid(0));
        } catch (Exception err) {
            return false;
        }
    }

    public int executeUpdate(String query) throws SQLException {

        var result = -1;
        Statement stm = null;

        stm = connection.createStatement();
        result = stm.executeUpdate(query);

        if (stm != null) {
            try {
                stm.close();
                stm = null;
            } catch (SQLException sqlerr) {
            }
        }

        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException sqlerr) {
            }
        }

        return result;

    }

    public ArrayList<Line> executeQuery(String query) throws SQLException {
        var listLineResult = new ArrayList<Line>();
        ResultSet set = null;
        Statement stm = null;

        stm = connection.createStatement();

        set = stm.executeQuery(query);

        var rsmd = set.getMetaData();

        while (set.next()) {
            var line = new Line();
            for (var i = 1; i <= rsmd.getColumnCount(); i++) {
                var lineField = new LineField();
                lineField.setFieldName(rsmd.getColumnName(i).toUpperCase());
                lineField.setFieldType(rsmd.getColumnTypeName(i).getClass());
                lineField.setFieldValue(set.getObject(i));
                line.AddLineField(lineField);
            }
            listLineResult.add(line);
        }

        if (stm != null) {
            try {
                stm.close();
                stm = null;
            } catch (SQLException sqlerr) {
            }
        }

        if (set != null) {
            try {
                set.close();
                set = null;
            } catch (SQLException sqlerr) {
            }
        }

        return listLineResult;
    }

}
