package ef.qb.cassandra_tests.unit.querybuilding;

import ef.qb.cassandra.exceptions.GetterNotFoundInClassException;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.CONTAINS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.ENDS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.NOT_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.STARTS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.filterBySpecialComparison;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.filterListBySpecialComparisonClause;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.hasCompareToNullAnnotationOnFields;
import ef.qb.cassandra_tests.testresources.CompareNullQueryObject;
import ef.qb.cassandra_tests.testresources.Person;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecialComparisonUtilsTest {

    @Test
    public void filterBySpecialComparisonNotEqualStringTest() {
        assertTrue(filterBySpecialComparison("Hello", "Hello to all", NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualStringTest() {
        assertFalse(filterBySpecialComparison("Hello", "Hello", NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualIntTest() {
        assertTrue(filterBySpecialComparison(1, 2, NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualIntTest() {
        assertFalse(filterBySpecialComparison(1, 1, NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonStartingWithStringTest() {
        assertTrue(filterBySpecialComparison("Hello", "He", STARTS));
    }

    @Test
    public void filterBySpecialComparisonNotStartingWithStringTest() {
        assertFalse(filterBySpecialComparison("Hello", "whatever", STARTS));
    }

    @Test
    public void filterBySpecialComparisonEndingWithStringTest() {
        assertTrue(filterBySpecialComparison("Hello", "lo", ENDS));
    }

    @Test
    public void filterBySpecialComparisonNotEndingWithStringTest() {
        assertFalse(filterBySpecialComparison("Hello", "whatever", ENDS));
    }

    @Test
    public void filterBySpecialComparisonContainingStringTest() {
        assertTrue(filterBySpecialComparison("Hello", "el", CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonNotContainingStringTest() {
        assertFalse(filterBySpecialComparison("Hello", "whatever", CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonWithNotAvailableAttributeTest() {
        var clause = new SpecialComparisonClause("whatever", NOT_EQUALS);
        clause.setValue("whatever");

        List<Person> list = new ArrayList<>();
        var p = new Person();
        p.setId(1);
        p.setLastName("testlastname");
        p.setAge(33);
        list.add(p);

        assertThrows(GetterNotFoundInClassException.class, () -> filterListBySpecialComparisonClause(list, clause));
    }

    @Test
    public void hasCompareToNullAnnotationWithAnnotationPresentTest() {
        assertTrue(hasCompareToNullAnnotationOnFields(new CompareNullQueryObject()));
    }

    @Test
    public void hasCompareToNullAnnotationWithAnnotationNotPresentTest() {
        assertFalse(hasCompareToNullAnnotationOnFields(new Person()));
    }

}