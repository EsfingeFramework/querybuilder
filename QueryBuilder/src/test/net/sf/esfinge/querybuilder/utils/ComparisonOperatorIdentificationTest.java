package net.sf.esfinge.querybuilder.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;

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
