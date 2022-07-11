package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryBuildingUtilitiesTest {


    @Test
    public void replaceQueryArgsWithOneArgTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id > ?";
        Object[] args = {1};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id > 1", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ?";
        Object[] args = {1, "Max"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id = 1 AND lastName = 'Max'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoStringArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = ? AND lastName = ?";
        Object[] args = {"Max", "Power"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max' AND lastName = 'Power'", newQuery);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInArrayTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ?";
        Object[] args = {1, "Max", "Additional"};

        QueryBuildingUtils.replaceQueryArgs(query, args);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInQueryTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = ? AND lastName = ? OR city = ?";
        Object[] args = {1, "Max"};

        QueryBuildingUtils.replaceQueryArgs(query, args);
    }


    @Test
    public void getValueRepresentationByTypeWithNumbersTest() {
        assertEquals("1", QueryBuildingUtils.getValueRepresentationByType(1));
        assertEquals("1.1", QueryBuildingUtils.getValueRepresentationByType(1.1));
    }

    @Test
    public void getValueRepresentationByTypeWithTextTest() {
        assertEquals("'Max'", QueryBuildingUtils.getValueRepresentationByType("Max"));
        assertEquals("'A'", QueryBuildingUtils.getValueRepresentationByType('A'));
    }
}