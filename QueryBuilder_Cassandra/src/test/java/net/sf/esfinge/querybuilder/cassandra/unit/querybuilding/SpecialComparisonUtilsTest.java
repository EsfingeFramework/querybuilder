package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialComparisonUtilsTest {

    @Test
    public void filterBySpecialComparisonNotEqualStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello", "Hello to all", SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello", "Hello", SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualIntTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison(1, 2, SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualIntTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison(1, 1, SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonStartingWithStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello", "He", SpecialComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonNotStartingWithStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello", "whatever", SpecialComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonEndingWithStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello", "lo", SpecialComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonNotEndingWithStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello", "whatever", SpecialComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonContainingStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello", "el", SpecialComparisonType.CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonNotContainingStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello", "whatever", SpecialComparisonType.CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonWithNotAvailableValueTest() {
        SpecialComparisonClause clause = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        clause.setValue("whatever");

        List<Person> list = new ArrayList<>();
        Person p = new Person();
        p.setId(1);
        p.setLastName("testlastname");
        p.setAge(33);
        list.add(p);

        assertThrows(MethodInvocationException.class, () -> SpecialComparisonUtils.filterListBySpecialComparisonClause(list, clause));
    }

}