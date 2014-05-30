package org.esfinge.querybuilder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.esfinge.querybuilder.exception.InvalidPropertyException;
import org.esfinge.querybuilder.methodparser.Person;
import org.junit.Test;

public class ReflectionUtilsTest {
	
	@Test
	public void simplePropertyType(){
		Class<?> type = ReflectionUtils.getPropertyType(Person.class, "name");
		assertEquals("The name property is a String",String.class, type);
	}
	
	@Test
	public void booleanPropertyType(){
		Class<?> type = ReflectionUtils.getPropertyType(Person.class, "authorized");
		assertEquals("The authorized property is a boolean",boolean.class, type);
	}
	
	@Test(expected=InvalidPropertyException.class)
	public void getTypeOfNonExistingProperty(){
		ReflectionUtils.getPropertyType(Person.class, "noExisting");
	}
	
	@Test
	public void compositePropertyType(){
		Class<?> type = ReflectionUtils.getPropertyType(Person.class, "address.number");
		assertEquals("The number property inside property address is int",int.class, type);
	}
	
	@Test
	public void existingProperty(){
		boolean exists = ReflectionUtils.existProperty(Person.class, "address");
		assertTrue("The property address exists",exists);
	}
	
	@Test
	public void nonExistingProperty(){
		boolean exists = ReflectionUtils.existProperty(Person.class, "other");
		assertFalse("The property other do not exist",exists);
	}
	
	@Test
	public void parameterNameWithoutAnything() throws SecurityException, NoSuchMethodException{
		Method m = ExampleQueryClass.class.getMethod("getName");
		String s = ReflectionUtils.getQueryParamenterName(m);
		assertEquals("nameEquals",s);
	}
	
	@Test
	public void parameterNameWithFieldAnnotation() throws SecurityException, NoSuchMethodException{
		Method m = ExampleQueryClass.class.getMethod("getAge");
		String s = ReflectionUtils.getQueryParamenterName(m);
		assertEquals("ageLesser",s);
	}
	
	@Test
	public void parameterNameWithGetterAnnotation() throws SecurityException, NoSuchMethodException{
		Method m = ExampleQueryClass.class.getMethod("getLastName");
		String s = ReflectionUtils.getQueryParamenterName(m);
		assertEquals("lastNameContains",s);
	}
	
	@Test
	public void parameterNameWithConvention() throws SecurityException, NoSuchMethodException{
		Method m = ExampleQueryClass.class.getMethod("getBirthDateGreater");
		String s = ReflectionUtils.getQueryParamenterName(m);
		assertEquals("birthDateGreater",s);
	}
	
	@Test
	public void createParameterMap(){
		Date date = new Date();
		
		ExampleQueryClass ex = new ExampleQueryClass();
		ex.setName("john");
		ex.setAge(20);
		ex.setBirthDateGreater(date);
		ex.setLastName("connor");
		
		Map<String, Object> map = ReflectionUtils.toParameterMap(ex);
		
		assertEquals(4,map.size());
		assertEquals("john",map.get("nameEquals"));
		assertEquals(20,map.get("ageLesser"));
		assertEquals(date,map.get("birthDateGreater"));
		assertEquals("connor",map.get("lastNameContains"));
	}
	

}
