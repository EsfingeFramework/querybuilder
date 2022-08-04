package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialComparisonUtilsTest {

    @Test
    public void filterBySpecialComparisonNotEqualStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello","Hello to all", SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello","Hello", SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualIntTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison(1,2, SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonNotEqualWithEqualIntTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison(1,1, SpecialComparisonType.NOT_EQUALS));
    }

    @Test
    public void filterBySpecialComparisonStartingWithStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello","He", SpecialComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonNotStartingWithStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello","whatever", SpecialComparisonType.STARTS));
    }

    @Test
    public void filterBySpecialComparisonEndingWithStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello","lo", SpecialComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonNotEndingWithStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello","whatever", SpecialComparisonType.ENDS));
    }

    @Test
    public void filterBySpecialComparisonContainingStringTest() {
        assertTrue(SpecialComparisonUtils.filterBySpecialComparison("Hello","el", SpecialComparisonType.CONTAINS));
    }

    @Test
    public void filterBySpecialComparisonNotContainingStringTest() {
        assertFalse(SpecialComparisonUtils.filterBySpecialComparison("Hello","whatever", SpecialComparisonType.CONTAINS));
    }




}