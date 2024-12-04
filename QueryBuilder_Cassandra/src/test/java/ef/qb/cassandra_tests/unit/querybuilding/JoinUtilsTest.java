package ef.qb.cassandra_tests.unit.querybuilding;

import ef.qb.cassandra.exceptions.GetterNotFoundInClassException;
import ef.qb.cassandra.exceptions.UnsupportedComparisonException;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.CONTAINS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.ENDS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.GREATER;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.GREATER_OR_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.LESSER;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.LESSER_OR_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.NOT_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.STARTS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinUtils.filterByJoinClauseComparisonType;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinUtils.filterListByJoinClause;
import ef.qb.cassandra_tests.testresources.Person;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JoinUtilsTest {

    @Test
    public void filterByJoinClauseEqualStringTest() {
        assertTrue(filterByJoinClauseComparisonType("Hello", "Hello", EQUALS));
    }

    @Test
    public void filterByJoinClauseEqualWithNotEqualStringTest() {
        assertFalse(filterByJoinClauseComparisonType("Hello", "Hellos", EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualStringTest() {
        assertTrue(filterByJoinClauseComparisonType("Hello", "Hello to all", NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualWithEqualStringTest() {
        assertFalse(filterByJoinClauseComparisonType("Hello", "Hello", NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualIntTest() {
        assertTrue(filterByJoinClauseComparisonType(1, 2, NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseNotEqualWithEqualIntTest() {
        assertFalse(filterByJoinClauseComparisonType(1, 1, NOT_EQUALS));
    }

    @Test
    public void filterByJoinClauseStartingWithStringTest() {
        assertTrue(filterByJoinClauseComparisonType("Hello", "He", STARTS));
    }

    @Test
    public void filterByJoinClauseNotStartingWithStringTest() {
        assertFalse(filterByJoinClauseComparisonType("Hello", "whatever", STARTS));
    }

    @Test
    public void filterByJoinClauseEndingWithStringTest() {
        assertTrue(filterByJoinClauseComparisonType("Hello", "lo", ENDS));
    }

    @Test
    public void filterByJoinClauseNotEndingWithStringTest() {
        assertFalse(filterByJoinClauseComparisonType("Hello", "whatever", ENDS));
    }

    @Test
    public void filterByJoinClauseContainingStringTest() {
        assertTrue(filterByJoinClauseComparisonType("Hello", "el", CONTAINS));
    }

    @Test
    public void filterByJoinClauseNotContainingStringTest() {
        assertFalse(filterByJoinClauseComparisonType("Hello", "whatever", CONTAINS));
    }

    @Test
    public void filterByJoinWithIntegersTest() {
        assertTrue(filterByJoinClauseComparisonType(1, 1, EQUALS));
        assertTrue(filterByJoinClauseComparisonType(2, 1, GREATER_OR_EQUALS));
        assertTrue(filterByJoinClauseComparisonType(2, 1, GREATER));
        assertTrue(filterByJoinClauseComparisonType(1, 2, LESSER_OR_EQUALS));
        assertTrue(filterByJoinClauseComparisonType(1, 2, LESSER));
    }

    @Test
    public void filterByJoinWithDoublesTest() {
        assertTrue(filterByJoinClauseComparisonType(1.1, 1.1, EQUALS));
        assertTrue(filterByJoinClauseComparisonType(1.1, 1.0, GREATER_OR_EQUALS));
        assertTrue(filterByJoinClauseComparisonType(1.1, 1.0, GREATER));
        assertTrue(filterByJoinClauseComparisonType(1.1, 2.1, LESSER_OR_EQUALS));
        assertTrue(filterByJoinClauseComparisonType(1.1, 2.1, LESSER));
    }

    @Test
    public void unsupportedComparisonTypeTest() {
        assertThrows(UnsupportedComparisonException.class, () -> filterByJoinClauseComparisonType(1.1, 1.1, STARTS));
        assertThrows(UnsupportedComparisonException.class, () -> filterByJoinClauseComparisonType(1, 1, CONTAINS));
    }

    @Test
    public void filterByJoinClauseWithNotAvailableAttributeTest() {
        var clause = new JoinClause("whatever", "whatever", NOT_EQUALS);
        clause.setValue("whatever");

        List<Person> list = new ArrayList<>();
        var p = new Person();
        p.setId(1);
        p.setLastName("testlastname");
        p.setAge(33);
        list.add(p);

        assertThrows(GetterNotFoundInClassException.class, () -> filterListByJoinClause(list, clause));
    }

}