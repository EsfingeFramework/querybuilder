package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryBuildingUtilitiesTest {


    @Test
    public void replaceQueryArgsWithOneArgTest() {
        String query = "SELECT * FROM Person WHERE id > ?";
        Object args[] = {1};

        String newQuery = QueryBuildingUtilities.replaceQueryArgs(query,args);

        assertEquals("SELECT * FROM Person WHERE id > 1", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoArgsTest() {
        String query = "SELECT * FROM Person WHERE id = ? AND lastName = ?";
        Object args[] = {1, "Max"};

        String newQuery = QueryBuildingUtilities.replaceQueryArgs(query,args);

        assertEquals("SELECT * FROM Person WHERE id = 1 AND lastName = 'Max'", newQuery);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInArrayTest() {
        String query = "SELECT * FROM Person WHERE id = ? AND lastName = ?";
        Object args[] = {1, "Max", "Additional"};

        String newQuery = QueryBuildingUtilities.replaceQueryArgs(query,args);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInQueryTest() {
        String query = "SELECT * FROM Person WHERE id = ? AND lastName = ? OR city = ?";
        Object args[] = {1, "Max"};

        String newQuery = QueryBuildingUtilities.replaceQueryArgs(query,args);
    }


    @Test
    public void getValueRepresentationByTypeWithNumbersTest() {
        assertEquals("1", QueryBuildingUtilities.getValueRepresentationByType(1));
        assertEquals("1.1", QueryBuildingUtilities.getValueRepresentationByType(1.1));
    }

    @Test
    public void getValueRepresentationByTypeWithTextTest() {
        assertEquals("'Max'", QueryBuildingUtilities.getValueRepresentationByType("Max"));
        assertEquals("'A'", QueryBuildingUtilities.getValueRepresentationByType('A'));
    }
}