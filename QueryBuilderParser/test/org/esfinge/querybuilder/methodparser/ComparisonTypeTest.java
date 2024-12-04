package org.esfinge.querybuilder.methodparser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ComparisonTypeTest {
	
	
	@Test
	public void testGreaterComparison(){
		String[] words = {"greater"};
		List<String> comparisonName = Arrays.asList(words);
		ComparisonType cp = ComparisonType.getComparisonType(comparisonName, 0);
		assertEquals("Type should be greater", cp, ComparisonType.GREATER);
	}
	
	@Test
	public void testLesserComparisonOtherIndex(){
		String[] words = {"other", "other", "lesser"};
		List<String> comparisonName = Arrays.asList(words);
		ComparisonType cp = ComparisonType.getComparisonType(comparisonName, 2);
		assertEquals("Type should be lesser", cp, ComparisonType.LESSER);
	}
	
	@Test
	public void testLesserOrEquals(){
		String[] words = {"other", "lesser","or","equals"};
		List<String> comparisonName = Arrays.asList(words);
		ComparisonType cp = ComparisonType.getComparisonType(comparisonName, 1);
		assertEquals("Type should be lesser or equals", cp, ComparisonType.LESSER_OR_EQUALS);
	}
	
	@Test
	public void defaultSituation(){
		String[] words = {"other", "other"};
		List<String> comparisonName = Arrays.asList(words);
		ComparisonType cp = ComparisonType.getComparisonType(comparisonName, 0);
		assertEquals("The default is equals", cp, ComparisonType.EQUALS);
	}
	
	@Test
	public void inTheEndReturnEquals(){
		String[] words = {"other", "other"};
		List<String> comparisonName = Arrays.asList(words);
		ComparisonType cp = ComparisonType.getComparisonType(comparisonName, 2);
		assertEquals("In the end return equals", cp, ComparisonType.EQUALS);
	}
	
	@Test
	public void wordNumber(){
		assertEquals("Equals should be 0", 0, ComparisonType.EQUALS.wordNumber());
		assertEquals("With one word should be 1", 1, ComparisonType.GREATER.wordNumber());
		assertEquals("Should return the number or words (2)", 2, ComparisonType.NOT_EQUALS.wordNumber());
		assertEquals("Should return the number or words (3)", 3, ComparisonType.LESSER_OR_EQUALS.wordNumber());
	}
	

}
