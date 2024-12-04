package org.esfinge.querybuilder.utils;

import static org.junit.Assert.*;

import org.esfinge.querybuilder.annotation.Greater;
import org.junit.Test;

public class ComparisonOperatorIdentificationTest {
	
	@Test
	public void testComparisonOperator(){
		boolean isComparisonOperator = ReflectionUtils.isComparisonOperator(Greater.class);
		assertTrue("GreaterThan has the comparison operator annotation", isComparisonOperator);
	}
	
	@Test
	public void testNotComparisonOperator(){
		boolean isComparisonOperator = ReflectionUtils.isComparisonOperator(Other.class);
		assertFalse("GreaterThan has the comparison operator annotation", isComparisonOperator);
	}

}
