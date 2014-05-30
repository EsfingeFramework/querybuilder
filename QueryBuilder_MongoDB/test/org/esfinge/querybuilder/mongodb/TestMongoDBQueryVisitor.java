package org.esfinge.querybuilder.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.esfinge.querybuilder.methodparser.DSLMethodParser;
import org.esfinge.querybuilder.methodparser.EntityClassProvider;
import org.esfinge.querybuilder.methodparser.MethodParser;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.utils.ServiceLocator;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class TestMongoDBQueryVisitor {
	
	QueryVisitor visitor;
	MethodParser mp = new DSLMethodParser();
	
	@Before
	public void init(){
		mp.setInterface(TestQuery.class);
		mp.setEntityClassProvider(ServiceLocator.getServiceImplementation(EntityClassProvider.class));
	}
	
	@Test
	public void singleEntity(){
		
		Method m = null;
		try {
			m = TestQuery.class.getMethod("getPerson", new Class[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), null);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ }", query);
	}
	
	@Test
	public void oneCondition(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("nome");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"name\" : \"nome\"}", query);
	}
	
	@Test
	public void greater(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAgeGreater", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new Integer(10);
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"age\" : { \"$gt\" : 10}}", query);
		
	}
	
	@Test
	public void greaterEquals(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = Integer.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByAgeAndName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new Integer(10);
		args[1] = "John";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"$and\" : [ { \"age\" : { \"$gte\" : 10}} , { \"name\" : \"John\"}]}", query);
		
	}
	
	@Test
	public void greaterFluent(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAgeGreater", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new Integer(10);
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"age\" : { \"$gt\" : 10}}", query);
		
	}
	
	@Test
	public void notEquals(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameNotEquals", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = "John";
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"name\" : { \"$ne\" : \"John\"}}", query);
		
	}
	
	@Test
	public void twoConditionsAND(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameAndLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals("{ \"$and\" : [ { \"name\" : \"nome\"} , { \"lastName\" : \"sobrenome\"}]}", query);
	}
	
	@Test
	public void twoConditionsOR(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameOrLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals("{ \"$or\" : [ { \"name\" : \"nome\"} , { \"lastName\" : \"sobrenome\"}]}", query);
	}
	
	@Test
	public void compositeCondition(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByAddressCity", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("cidade");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals("{ \"address.city\" : \"cidade\"}", query);
	}
	
	@Test
	public void complexQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[3];
			args[0] = String.class;
			args[1] = String.class;
			args[2] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameOrLastNameAndAddressCity", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[3];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		args[2] = new String("cidade");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals("{ \"$or\" : [ { \"name\" : \"nome\"} , { \"$and\" : [ { \"lastName\" : \"sobrenome\"} , { \"address.city\" : \"cidade\"}]}]}", query);
	}
	
	@Test
	public void fixParameterQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[0];
			m = TestQuery.class.getMethod("getPersonCarioca", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), null);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"address.city\" : \"Rio de Janeiro\"}", query);
		
		assertEquals(qr.getFixParameterValue("cityEQUALS"), "Rio de Janeiro");
		assertTrue(qr.getFixParameters().contains("cityEQUALS"));
	}
	
	@Test
	public void mixedWithfixParameterQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonCariocaByName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("nome");
		
		visitor = MongoDBVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		
		assertEquals("{ \"$and\" : [ { \"address.city\" : \"Rio de Janeiro\"} , { \"name\" : \"nome\"}]}", query);
		assertEquals(qr.getFixParameterValue("cityEQUALS"), "Rio de Janeiro");
		assertTrue(qr.getFixParameters().contains("cityEQUALS"));
	}
	
}
