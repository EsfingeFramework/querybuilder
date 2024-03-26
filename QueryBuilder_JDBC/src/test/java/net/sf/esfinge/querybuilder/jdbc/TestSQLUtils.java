package net.sf.esfinge.querybuilder.jdbc;

import net.sf.esfinge.querybuilder.utils.SQLUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestSQLUtils {

    private final SQLUtils tsu = new SQLUtils("Person");
    private final SQLUtils tsuWorker = new SQLUtils("Worker");

    @Test
    public void testEntitiesSearch() {

        var entities = tsu.getChildEntities();
        assertEquals("Should retrieve Person and Adress", "person, address",
                entities);
    }

    @Test
    public void testFieldsSearch() {

        var fields = tsu.getFieldsEntities();
        assertEquals(
                "Should retrieve Person and Adress fields",
                "person.id, person.name, person.lastName, person.age, address.id, address.city, address.state",
                fields);
    }

    @Test
    public void testListOfJoinColumns() {

        String fields = null;

        for (var str : tsu.getListOfJoinColumns()) {

            fields = str;

        }

        assertEquals("ADDRESS_ID", fields);

        assertEquals(true, tsu.isJoinColumn(fields));

    }

    @Test
    public void testJoinListExpressions() {

        var joinExpression = tsu.getJoinExpressions();
        assertEquals("Should retrieve person.address_id = address.id",
                "person.address_id = address.id", joinExpression);
    }

    @Test
    public void testColumnsToInsertCommands() {
        var columns = tsu.getColumnsToInsertComand();
        assertEquals("(id,name,lastname,age,address_id)", columns);
    }

    @Test
    public void testColumnsToUpdateCommands() {
        var columns = new StringBuilder();

        columns.append("(");

        for (var c : tsu.getListOfColumnsToUpdateComand()) {

            columns.append(c.toLowerCase());
            columns.append(",");

        }

        columns.delete((columns.length() - 1), columns.length());
        columns.append(")");

        assertEquals("(name,lastname,age,address_id)", columns.toString());
    }

    @Test
    public void testPrimaryKey() {
        var pKey = tsu.getPrimaryKeyOfMainEntity();
        assertEquals("id", pKey);
    }

    @Test
    public void testEntitiesSearchWorker() {

        var entities = tsuWorker.getChildEntities();
        assertEquals("Should retrieve Worker", "worker", entities);
    }

    @Test
    public void testFieldsSearchWorker() {

        var fields = tsuWorker.getFieldsEntities();
        assertEquals("Should retrieve Worker fields",
                "worker.id, worker.name, worker.lastName, worker.age", fields);
    }

    @Test
    public void testJoinListExpressionsWorker() {

        var joinExpression = tsuWorker.getJoinExpressions();
        assertEquals("", joinExpression);
    }

    @Test
    public void testColumnsToInsertCommandsWorker() {
        var columns = tsuWorker.getColumnsToInsertComand();
        assertEquals("(id,name,lastname,age)", columns);
    }

    @Test
    public void testPrimaryKeyWorker() {
        var pKey = tsuWorker.getPrimaryKeyOfMainEntity();
        assertEquals("id", pKey);
    }

}
