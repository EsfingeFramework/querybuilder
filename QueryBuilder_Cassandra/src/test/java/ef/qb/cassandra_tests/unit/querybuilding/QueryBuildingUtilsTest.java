package ef.qb.cassandra_tests.unit.querybuilding;

import ef.qb.cassandra.exceptions.QueryParametersMismatchException;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.extractParameterNameFromParameterWithComparison;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.getComparisonType;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.getValueRepresentationByType;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.replaceQueryArgs;
import static ef.qb.core.methodparser.ComparisonType.EQUALS;
import static ef.qb.core.methodparser.ComparisonType.GREATER;
import static ef.qb.core.methodparser.ComparisonType.LESSER_OR_EQUALS;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QueryBuildingUtilsTest {

    @Test
    public void replaceQueryArgsWithOneArgTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE id > 0?";
        Object[] args = {1};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id > 1", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoArgsTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = 0? AND lastName = 1?";
        Object[] args = {1, "Max"};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id = 1 AND lastName = 'Max'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithTwoStringArgsTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND lastName = 1?";
        Object[] args = {"Max", "Power"};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max' AND lastName = 'Power'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithSkippedArgsTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND lastName = 2?";
        Object[] args = {"Max", null, "Power"};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max' AND lastName = 'Power'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithNullValueArgTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person";
        Object[] args = {null};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person", newQuery);
    }

    @Test
    public void replaceQueryArgsWithNullAndNonNullValueArgsTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE name = 1?";
        Object[] args = {null, "Max"};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Max'", newQuery);
    }

    @Test
    public void replaceQueryArgsWithMoreArgsInArrayTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = 0? AND name = 1?";
        Object[] args = {1, "Max", "Additional"};

        var newQuery = replaceQueryArgs(query, args);

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE id = 1 AND name = 'Max'", newQuery);
    }

    @Test(expected = QueryParametersMismatchException.class)
    public void replaceQueryArgsWithMoreArgsInQueryTest() {
        var query = "SELECT * FROM <#keyspace-name#>.Person WHERE id = 0? AND lastName = 1? OR city = 2?";
        Object[] args = {1, "Max"};

        replaceQueryArgs(query, args);
    }

    @Test
    public void getValueRepresentationByTypeWithNumbersTest() {
        assertEquals("1", getValueRepresentationByType(1));
        assertEquals("1.1", getValueRepresentationByType(1.1));
    }

    @Test
    public void getValueRepresentationByTypeWithTextTest() {
        assertEquals("'Max'", getValueRepresentationByType("Max"));
        assertEquals("'A'", getValueRepresentationByType('A'));
    }

    @Test
    public void getParameterNameFromParameterWithComparisonTest() {
        assertEquals("name", extractParameterNameFromParameterWithComparison("nameEquals"));
        assertEquals("age", extractParameterNameFromParameterWithComparison("ageLesser"));
        assertEquals("lastName", extractParameterNameFromParameterWithComparison("lastNameLesserOrEquals"));
    }

    @Test
    public void getComparisonTypeTest() {
        assertEquals(EQUALS, getComparisonType("lastNameEquals"));
        assertEquals(GREATER, getComparisonType("lastNameGreater"));
        assertEquals(LESSER_OR_EQUALS, getComparisonType("lastNameLesserOrEquals"));
    }

    @Test
    public void getComparisonTypeWithComparisonTypeNotFoundTest() {
        assertNull(getComparisonType("lastNameEqualsWhatever"));
        assertNull(getComparisonType("lastName"));
        assertNull(getComparisonType("lastNameEqualseee"));
    }
}
