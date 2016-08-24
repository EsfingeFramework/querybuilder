package org.esfinge.querybuilder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.Starts;
import org.esfinge.querybuilder.exception.ParameterAnnotationNotFoundException;
import org.junit.Test;

public class ContainsParameterAnnotationTest {
	
	public interface MockClass{
		public void method1(@CompareToNull int param1, @IgnoreWhenNull String param2);
		public void method2(@CompareToNull @IgnoreWhenNull int param);
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	public void containsParameterAnnotation() throws Exception{
		Method method = MockClass.class.getMethod("method1", int.class, String.class);
		boolean response = ReflectionUtils.containsParameterAnnotation(method, CompareToNull.class);
		assertTrue(response);
	}
	
	@Test
	public void notContainsParameterAnnotation() throws Exception{
		Method method = MockClass.class.getMethod("method1", int.class, String.class);
		boolean response = ReflectionUtils.containsParameterAnnotation(method, Starts.class);
		assertFalse(response);
	}
	
	@Test
	public void getParameterAnnotationIndex() throws Exception{
		Method methodOne = MockClass.class.getMethod("method1", int.class, String.class);
		
		int methodOneCompareToNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodOne, CompareToNull.class);
		assertEquals(0, methodOneCompareToNullParamIndex);
		int methodOneIgnoreWhenNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodOne, IgnoreWhenNull.class);
		assertEquals(1, methodOneIgnoreWhenNullParamIndex);
		
		Method methodTwo = MockClass.class.getMethod("method2", int.class);
		
		int methodTwoCompareToNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodTwo, CompareToNull.class);
		assertEquals(0, methodTwoCompareToNullParamIndex);
		int methodTwoIgnoreWhenNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodTwo, IgnoreWhenNull.class);
		assertEquals(0, methodTwoIgnoreWhenNullParamIndex);
	}
	
	@Test
	public void getParameterAnnotationIndexException() throws Exception{
		final Method m = MockClass.class.getMethod("method1", int.class, String.class);
		new AssertException(ParameterAnnotationNotFoundException.class, "The method method1 has no parameter with org.esfinge.querybuilder.annotation.Starts annotation") {
			protected void run() {
				ReflectionUtils.getParameterAnnotationIndex(m, Starts.class);
			}
		};
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	public void haveAndNotHaveParam1() throws Exception{
		Method method = MockClass.class.getMethod("method1",int.class, String.class);
		boolean bol1 = ReflectionUtils.containsParameterAnnotation(method, 0, CompareToNull.class);
		boolean bol2 = ReflectionUtils.containsParameterAnnotation(method, 0, IgnoreWhenNull.class);
		assertTrue("Parameter 1 contains @CompareToNull",bol1);
		assertFalse("Parameter 1 does not contain @IgnoreWhenNull",bol2);
	}
	
	@Test
	public void haveAndNotHaveParam2() throws Exception{
		Method method = MockClass.class.getMethod("method1",int.class, String.class);
		boolean bol1 = ReflectionUtils.containsParameterAnnotation(method, 1, CompareToNull.class);
		boolean bol2 = ReflectionUtils.containsParameterAnnotation(method, 1, IgnoreWhenNull.class);
		assertFalse("Parameter 2 does not contain @CompareToNull",bol1);
		assertTrue("Parameter 2 contains @IgnoreWhenNull",bol2);
	}
	
	@Test
	public void haveBoth() throws Exception{
		Method method = MockClass.class.getMethod("method2",int.class);
		boolean bol1 = ReflectionUtils.containsParameterAnnotation(method, 0, CompareToNull.class);
		boolean bol2 = ReflectionUtils.containsParameterAnnotation(method, 0, IgnoreWhenNull.class);
		assertTrue("Parameter contains @CompareToNull",bol1);
		assertTrue("Parameter contains @IgnoreWhenNull",bol2);
	}

}
