package org.esfinge.querybuilder.methodparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.classmock.Annotation;
import net.sf.classmock.ClassMock;
import net.sf.classmock.Location;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.Contains;
import org.esfinge.querybuilder.annotation.Greater;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.QueryObject;
import org.esfinge.querybuilder.exception.InvalidPropertyException;
import org.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import org.esfinge.querybuilder.exception.QueryObjectException;
import org.esfinge.querybuilder.exception.WrongParamNumberException;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.esfinge.querybuilder.utils.AssertException;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.States;
import org.junit.Before;
import org.junit.Test;

public class QueryObjectMethodParserTest extends MethodParserTest{
	
	protected ClassMock queryMockClass;
	protected Class queryClass;
	
	@Before
	public void createQueryObject(){
		queryMockClass = new ClassMock("QueryClass");
	}
	
	protected  MethodParser createParserClass() {
		return new QueryObjectMethodParser();
	}
	
	@Test
	public void doNotVerifyMatchingWithoutParams() throws Exception{
		Method m = createMethodForTesting(List.class, "getPerson");
		boolean match = parser.fitParserConvention(m);
		assertFalse("Should not match", match);
	}
	
	@Test
	public void doNotVerifyMatchingWithtParam() throws Exception{
		Method m = createMethodForTesting(List.class, "getPerson", String.class);
		boolean match = parser.fitParserConvention(m);
		assertFalse("Should not match", match);
	}
	
	@Test
	public void verifyMatchingWithBySomething() throws Exception{
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(List.class, "getPersonByName", QueryObject.class, queryClass);
		boolean match = parser.fitParserConvention(m);
		assertFalse("Should not match", match);
	}
	
	@Test
	public void verifyMatching() throws Exception{
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(List.class, "getPerson", QueryObject.class, queryClass);
		boolean match = parser.fitParserConvention(m);
		assertTrue("Should match", match);
	}
	
	@Test
	public void getListEntityName() throws Exception{
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(List.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m);
		assertEquals("Entity should be Person", "Person", qi.getEntityName());
		assertEquals("Entity should be Person", Person.class, qi.getEntityType());
		assertEquals("The query type should be RETRIEVE_LIST", QueryType.RETRIEVE_LIST, qi.getQueryType());	
	}
	
	@Test
	public void getSingleEntityName() throws Exception{
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m);
		assertEquals("The query type should be RETRIEVE_SINGLE", QueryType.RETRIEVE_SINGLE, qi.getQueryType());	
	}
	
	@Test
	public void queryWithOneRegularProperty() throws Exception{
		queryMockClass.addProperty("name", String.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m);
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithOneRegularMethod() throws Exception{
		queryMockClass.addMethod(String.class, "getName");
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m);
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithNoExistingProperty() throws Exception{
		queryMockClass.addProperty("unknown", String.class);
		queryClass = queryMockClass.createClass();
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		new AssertException(QueryObjectException.class, InvalidPropertyException.class) {
			protected void run() {
				QueryInfo qi = parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryWithNoExistingMethod() throws Exception{
		queryMockClass.addMethod(String.class, "getUnknown");
		queryClass = queryMockClass.createClass();
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		new AssertException(QueryObjectException.class, InvalidPropertyException.class) {
			protected void run() {
				QueryInfo qi = parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryWithWrongTypeProperty() throws Exception{
		queryMockClass.addProperty("name", int.class);
		queryClass = queryMockClass.createClass();
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		new AssertException(QueryObjectException.class, InvalidPropertyTypeException.class) {
			protected void run() {
				QueryInfo qi = parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryWithWrongTypeMerhod() throws Exception{
		queryMockClass.addMethod(int.class, "getName");
		queryClass = queryMockClass.createClass();
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		new AssertException(QueryObjectException.class, InvalidPropertyTypeException.class) {
			protected void run() {
				QueryInfo qi = parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryWithMoreThanOneProperty() throws Exception{
		queryMockClass.addProperty("name", String.class);
		queryMockClass.addProperty("lastName", String.class);
		queryMockClass.addProperty("age", int.class);
		queryMockClass.addProperty("addressCity", String.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.city", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithPropertiesAndMethods() throws Exception{
		queryMockClass.addMethod(String.class, "getName");
		queryMockClass.addMethod(String.class, "getLastName");
		queryMockClass.addProperty("age", int.class);
		queryMockClass.addProperty("addressCity", String.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.city", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithComparisonTypesOnProperties() throws Exception{
		queryMockClass.addProperty("nameContains", String.class);
		queryMockClass.addProperty("ageGreater", int.class);
		queryMockClass.addProperty("addressCityStarts", String.class);
		queryMockClass.addProperty("addressNumberLesserOrEquals", int.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.GREATER);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.city", ComparisonType.STARTS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.number", ComparisonType.LESSER_OR_EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithComparisonTypesOnMethods() throws Exception{
		queryMockClass.addMethod(String.class, "getNameContains");
		queryMockClass.addMethod(int.class, "getAgeGreater");
		queryMockClass.addMethod(String.class, "getAddressCityStarts");
		queryMockClass.addMethod(int.class, "getAddressNumberLesserOrEquals");
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.GREATER);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.city", ComparisonType.STARTS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.number", ComparisonType.LESSER_OR_EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithComparisonTypesUsingAnnotations() throws Exception{
		queryMockClass.addProperty("name", String.class);
		queryMockClass.addAnnotation("name", Contains.class, Location.FIELD);
		queryMockClass.addProperty("addressNumber", int.class);
		queryMockClass.addAnnotation("addressNumber", Greater.class, Location.GETTER);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.number", ComparisonType.GREATER);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithComparisonTypesUsingAnnotationsOnMethods() throws Exception{
		queryMockClass.addMethod(String.class, "getName");
		queryMockClass.addMethodAnnotation("getName", Contains.class);
		queryMockClass.addMethod(int.class, "getAddressNumber");
		queryMockClass.addMethodAnnotation("getAddressNumber", Greater.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.CONTAINS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("address.number", ComparisonType.GREATER);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithNullOptions() throws Exception{
		queryMockClass.addProperty("name", String.class);
		queryMockClass.addAnnotation("name", IgnoreWhenNull.class, Location.FIELD);
		queryMockClass.addProperty("lastNameStarts", String.class);
		queryMockClass.addAnnotation("lastNameStarts", CompareToNull.class, Location.GETTER);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.STARTS, NullOption.COMPARE_TO_NULL);when(s.is("condicao"));then(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithOrderBy() throws Exception{
		queryMockClass.addProperty("name", String.class);
		queryMockClass.addProperty("age", int.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPersonOrderByLastNameDesc", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitOrderBy("lastName", OrderingDirection.DESC);when(s.is("conector"));then(s.is("final"));
    	 	one(visitorMock).visitEnd(); when(s.is("final"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryMethodsWithOrderBy() throws Exception{
		queryMockClass.addMethod(String.class, "getName");
		queryMockClass.addMethod(int.class, "age");
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPersonOrderByLastNameDesc", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitOrderBy("lastName", OrderingDirection.DESC);when(s.is("conector"));then(s.is("final"));
    	 	one(visitorMock).visitEnd(); when(s.is("final"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void queryWithDomainTerm() throws Exception{
		Annotation an = domainTermWithOneCondition("underage","age", "18", ComparisonType.LESSER);
		mockClass.addAnnotation(an);
		queryMockClass.addProperty("name", String.class);
		queryMockClass.addProperty("lastNameStarts", String.class);
		queryClass = queryMockClass.createClass();
		Method m = createMethodWithAnnotationForTesting(Person.class, "getPersonUnderage", QueryObject.class, queryClass);
		QueryInfo qi = parser.parse(m); 
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	States s = context.states("QUERY").startsAs("inicio");
    	 	one(visitorMock).visitEntity("Person");when(s.is("inicio"));then(s.is("condicao"));
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.STARTS);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitCondition("age", ComparisonType.LESSER, 18);when(s.is("condicao"));then(s.is("conector"));
    	 	one(visitorMock).visitEnd(); when(s.is("conector"));
    	 	allowing(visitorMock).visitConector("and");when(s.is("conector"));then(s.is("condicao"));
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	public void queryWithMoreThanOneParameter() throws Exception{
		queryMockClass.addMethod(Person.class, "getPerson");
		queryClass = queryMockClass.createClass();
		
		mockClass.addAbstractMethod(queryClass, "getPerson", queryClass, Boolean.class);
		mockClass.addMethodParamAnnotation(0, "getPerson", QueryObject.class);
		
		Class<?> c = mockClass.createClass();
		parser = createParserClass();
		parser.setInterface(c);
		
		parser.setEntityClassProvider(classProviderMock);
		final Method m = c.getMethod("getPerson", queryClass, Boolean.class);
		
		new AssertException(WrongParamNumberException.class, "The method getPerson is using @QueryObject annotation but have more than one parameter") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

}
