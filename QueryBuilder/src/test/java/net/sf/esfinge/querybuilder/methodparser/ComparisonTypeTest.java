package net.sf.esfinge.querybuilder.methodparser;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComparisonTypeTest {

    @Test
    public void testGreaterComparison() {
        String[] words = {"greater"};
        var comparisonName = Arrays.asList(words);
        var cp = ComparisonType.getComparisonType(comparisonName, 0);
        assertEquals("Type should be greater", cp, ComparisonType.GREATER);
    }

    @Test
    public void testLesserComparisonOtherIndex() {
        String[] words = {"other", "other", "lesser"};
        var comparisonName = Arrays.asList(words);
        var cp = ComparisonType.getComparisonType(comparisonName, 2);
        assertEquals("Type should be lesser", cp, ComparisonType.LESSER);
    }

    @Test
    public void testLesserOrEquals() {
        String[] words = {"other", "lesser", "or", "equals"};
        var comparisonName = Arrays.asList(words);
        var cp = ComparisonType.getComparisonType(comparisonName, 1);
        assertEquals("Type should be lesser or equals", cp, ComparisonType.LESSER_OR_EQUALS);
    }

    @Test
    public void defaultSituation() {
        String[] words = {"other", "other"};
        var comparisonName = Arrays.asList(words);
        var cp = ComparisonType.getComparisonType(comparisonName, 0);
        assertEquals("The default is equals", cp, ComparisonType.EQUALS);
    }

    @Test
    public void inTheEndReturnEquals() {
        String[] words = {"other", "other"};
        var comparisonName = Arrays.asList(words);
        var cp = ComparisonType.getComparisonType(comparisonName, 2);
        assertEquals("In the end return equals", cp, ComparisonType.EQUALS);
    }

    @Test
    public void wordNumber() {
        assertEquals("Equals should be 0", 0, ComparisonType.EQUALS.wordNumber());
        assertEquals("With one word should be 1", 1, ComparisonType.GREATER.wordNumber());
        assertEquals("Should return the number or words (2)", 2, ComparisonType.NOT_EQUALS.wordNumber());
        assertEquals("Should return the number or words (3)", 3, ComparisonType.LESSER_OR_EQUALS.wordNumber());
    }

}
