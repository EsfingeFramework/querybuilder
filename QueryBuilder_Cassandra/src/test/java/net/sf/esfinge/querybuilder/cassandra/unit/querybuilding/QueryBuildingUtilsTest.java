package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QueryBuildingUtilsTest {


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

    @Test
    public void replaceQueryArgsWithNullValueArgTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person";
        Object[] args = {null};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person", newQuery);
    }

    @Test
    public void replaceQueryArgsWithNullAndNonNullValueArgsTest() {
        String query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = ?";
        Object[] args = {null, "Max"};

        String newQuery = QueryBuildingUtils.replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max'", newQuery);
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

    @Test
    public void getParameterNameFromParameterWithComparisonTest() {
        assertEquals("name", QueryBuildingUtils.extractParameterNameFromParameterWithComparison("nameEquals"));
        assertEquals("age", QueryBuildingUtils.extractParameterNameFromParameterWithComparison("ageLesser"));
        assertEquals("lastName", QueryBuildingUtils.extractParameterNameFromParameterWithComparison("lastNameLesserOrEquals"));
    }

    @Test
    public void getComparisonTypeTest() {
        assertEquals(ComparisonType.EQUALS, QueryBuildingUtils.getComparisonType("lastNameEquals"));
        assertEquals(ComparisonType.GREATER, QueryBuildingUtils.getComparisonType("lastNameGreater"));
        assertEquals(ComparisonType.LESSER_OR_EQUALS, QueryBuildingUtils.getComparisonType("lastNameLesserOrEquals"));
    }

    @Test
    public void getComparisonTypeWithComparisonTypeNotFoundTest() {
        assertNull(QueryBuildingUtils.getComparisonType("lastNameEqualsWhatever"));
        assertNull(QueryBuildingUtils.getComparisonType("lastName"));
        assertNull(QueryBuildingUtils.getComparisonType("lastNameEqualseee"));
    }
}