package net.sf.esfinge.querybuilder.methodparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.esfinge.classmock.Annotation;
import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Contains;
import net.sf.esfinge.querybuilder.annotation.DomainTerms;
import net.sf.esfinge.querybuilder.annotation.Ends;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.GreaterOrEquals;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.InvariablePageSize;
import net.sf.esfinge.querybuilder.annotation.Lesser;
import net.sf.esfinge.querybuilder.annotation.LesserOrEquals;
import net.sf.esfinge.querybuilder.annotation.NotEquals;
import net.sf.esfinge.querybuilder.annotation.PageNumber;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.annotation.VariablePageSize;
import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.exception.InvalidPaginationAnnotationSchemeException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.DSLMethodParser;
import net.sf.esfinge.querybuilder.methodparser.MethodParser;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import net.sf.esfinge.querybuilder.utils.AssertException;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DSLMethodParserTest extends MethodParserTest {
	
	protected  MethodParser createParserClass() {
		return new DSLMethodParser();
	}
	
	@Test
	public void simpleRetrivalParsing() throws Exception{
		Method m = createMethodForTesting(List.class, "getPerson");
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be Person", "Person", qi.getEntityName());
		assertEquals("The query type should be RETRIEVE_LIST", QueryType.RETRIEVE_LIST, qi.getQueryType());
	}
	 
	@Test
	public void entityCompositeName() throws Exception{
		Method m = createMethodForTesting(Object.class, "getJuridicPerson");
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be JuridicPerson", "JuridicPerson", qi.getEntityName());
		assertEquals("The query type should be RETRIEVE_LIST", QueryType.RETRIEVE_SINGLE, qi.getQueryType());
	}
	
	@Test
	public void oneParameterWithSimpleName() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByName", String.class);
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be Person", "Person", qi.getEntityName());
		assertEquals("There is a parameter called name", "name", qi.getCondition().getParameterNames().get(0));
	}
	
	@Test(expected=WrongParamNumberException.class)
	public void validateParamNumber() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByName");
		parser.parse(m);
	}
	
	@Test(expected=InvalidPropertyTypeException.class)
	public void validateParamType() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByName", int.class);
		parser.parse(m);
	}
	
	@Test(expected=InvalidPropertyException.class)
	public void validatePropName() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByUnknown", int.class);
		parser.parse(m);
	}
	
	/* RContribution: typo on method */
	@Test
	public void typoOnParamName() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNam", String.class);
		// Espera-se a excecao InvalidPropertyException
		// e que sua mensagem contenha a string "Nam"
		new AssertException(InvalidPropertyException.class, "Nam") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

	@Test
	public void typoOnOrderByParamName() throws Exception {
		final Method m = createMethodForTesting(Object.class, "getPersonOrderByNam");
		//It is expected a InvalidPropertyException containing a "Nam" String in its message
		new AssertException(InvalidPropertyException.class, "Nam") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

	@Test
	public void typoOnGet() throws Exception {
		final Method m = createMethodForTesting(Object.class, "etPerson");
		//It is expected a InvalidQuerySequenceException containing a "et" String in its message
		new AssertException(InvalidQuerySequenceException.class, "et") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoOnParamEmail() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndEail", String.class, String.class);
		new AssertException(InvalidPropertyException.class, "Eail") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoOnParamEmailAccusedBeforeNumberOfParam() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndEail", String.class);
		new AssertException(InvalidPropertyException.class, "Eail") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoMissingLetterInEntityName() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersoByName", String.class);
		new AssertException(EntityClassNotFoundException.class, "Perso") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoExtraLetterInEntityName() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonaByName", String.class);
		new AssertException(EntityClassNotFoundException.class, "Persona") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoChangeLetterInEntityName() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPergonByName", String.class);
		new AssertException(EntityClassNotFoundException.class, "Pergon") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoInGet() throws Exception{
		final Method m = createMethodForTesting(Object.class, "gtPersonByName", String.class);
		new AssertException(InvalidQuerySequenceException.class, "gt") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void typoInOrder() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameOrwerByAge", String.class);
		new AssertException(InvalidPropertyException.class, "OrwerByAge") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

	@Test
	public void typoInOrderBy() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameOrderBAge", String.class);
		new AssertException(InvalidPropertyException.class, "OrderBAge") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

	@Test
	public void typoMissingEntityName() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getByNameOrderByAge", String.class);
		new AssertException(EntityClassNotFoundException.class, "") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	/* RContribution: wrong property type */
	@Test(expected=InvalidPropertyTypeException.class)
	public void wrongPropertyTypeInteger() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByName", int.class);
		parser.parse(m);
	}
	
	@Test(expected=InvalidPropertyTypeException.class)
	public void wrongPropertyTypeString() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByAge", String.class);
		parser.parse(m);
	}
	
	@Test(expected=WrongParamNumberException.class)
	public void wrongExtraProperty() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndAge", String.class, int.class, int.class);
		parser.parse(m);
	}
	
	@Test(expected=InvalidPropertyTypeException.class)
	public void wrongPropertyOrder() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndAge", int.class, String.class);
		parser.parse(m);
	}
	
	@Test(expected=InvalidPropertyTypeException.class)
	public void wrongSecondType() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndAge", String.class, String.class);
		parser.parse(m);
	}
	
	@Test(expected=WrongParamNumberException.class)
	public void missingSecondType() throws Exception{
		final Method m = createMethodForTesting(Object.class, "getPersonByNameAndAge", String.class);
		parser.parse(m);
	}
	
	@Test
	public void oneParameterWithCompositeName() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByLastName", String.class);
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be Person", "Person", qi.getEntityName());
		assertEquals("There is a parameter called lastName", "lastName", qi.getCondition().getParameterNames().get(0));
	} 
	
	@Test
	public void oneCompositeParameter() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByAddressNumber", int.class);
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be Person", "Person", qi.getEntityName());
		assertEquals("There is a parameter called address.number", "address.number", qi.getCondition().getParameterNames().get(0));
	} 
	
	@Test
	public void doubleCompositeParameter() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByAddressStreetRoad", String.class);
		QueryInfo qi = parser.parse(m);
		assertEquals("The entity name should be Person", "Person", qi.getEntityName());
		assertEquals("There is a parameter called address.street.road", "address.street.road", qi.getCondition().getParameterNames().get(0));
	} 
	
	@Test
	public void andConditions() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByNameAndAge", String.class, int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orConditions() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByNameOrAge", String.class, int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitConector("or");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
	    }});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void andOrConditions() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByNameOrLastNameAndAge", String.class, String.class, int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitConector("or");inSequence(sequence);
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void parameterNames() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByNameOrLastNameAndAddressNumber", String.class, String.class, int.class);
		QueryInfo qi = parser.parse(m);
		List<String> paramNames = qi.getNamedParemeters();
		assertEquals("The query have 3 parameters", 3, paramNames.size());
		assertEquals("First parameter should be name", "nameEquals", paramNames.get(0));
		assertEquals("Second parameter should be lastName", "lastNameEquals", paramNames.get(1));
		assertEquals("Third parameter should be number", "addressNumberEquals", paramNames.get(2));
	}
	
	@Test
	public void conditionTypes() throws Exception{
		executeConditionTypes(Greater.class, ComparisonType.GREATER,1);
		executeConditionTypes(Lesser.class, ComparisonType.LESSER,2);
		executeConditionTypes(GreaterOrEquals.class, ComparisonType.GREATER_OR_EQUALS,3);
		executeConditionTypes(LesserOrEquals.class, ComparisonType.LESSER_OR_EQUALS,4);
		executeConditionTypes(NotEquals.class, ComparisonType.NOT_EQUALS,5);
		executeConditionTypes(Contains.class, ComparisonType.CONTAINS,6);
		executeConditionTypes(Starts.class, ComparisonType.STARTS,7);
		executeConditionTypes(Ends.class, ComparisonType.ENDS,8);
	}
	
	private void executeConditionTypes(Class annotation, final ComparisonType expectedType, int mockNumber) throws NoSuchMethodException {
		mockClass = new ClassMock("ExampleInterface", true);
		Method m = createMethodWithAnnotationForTesting(Object.class, "getPersonByAge", annotation, int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class,"QueryVisitor"+mockNumber);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", expectedType);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);
	}
	
	@Test
	public void conditionTypesOnMethodNames() throws Exception{
		executeConditionTypesWithMethodNamed("getPersonByAgeGreater", ComparisonType.GREATER,1, int.class, "age");
		executeConditionTypesWithMethodNamed("getPersonByAgeLesser", ComparisonType.LESSER,2, int.class, "age");
		executeConditionTypesWithMethodNamed("getPersonByAgeGreaterOrEquals", ComparisonType.GREATER_OR_EQUALS,3, int.class, "age");
		executeConditionTypesWithMethodNamed("getPersonByAgeLesserOrEquals", ComparisonType.LESSER_OR_EQUALS,4, int.class, "age");
		executeConditionTypesWithMethodNamed("getPersonByAgeNotEquals", ComparisonType.NOT_EQUALS,5, int.class, "age");
		executeConditionTypesWithMethodNamed("getPersonByNameContains", ComparisonType.CONTAINS,6, String.class, "name");
		executeConditionTypesWithMethodNamed("getPersonByNameStarts", ComparisonType.STARTS,7, String.class, "name");
		executeConditionTypesWithMethodNamed("getPersonByNameEnds", ComparisonType.ENDS,8, String.class, "name");
	}

	private void executeConditionTypesWithMethodNamed(String methodName,final ComparisonType expectedType, int mockNumber, Class parameterType, final String property) throws NoSuchMethodException {
		mockClass = new ClassMock("ExampleInterface", true);
		Method m = createMethodForTesting(Object.class, methodName, parameterType);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class,"QueryVisitor"+mockNumber);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition(property, expectedType);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);
	}

	@Test
	public void andOrConditionsWithOperators() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByNameGreaterOrLastNameNotEqualsAndAgeLesserOrEquals", String.class, String.class, int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.GREATER);inSequence(sequence);
    	 	one(visitorMock).visitConector("or");inSequence(sequence);
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.NOT_EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.LESSER_OR_EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void simpleDomainTerm() throws Exception{
		Annotation an = domainTermWithOneCondition("manager","job", "MAN", ComparisonType.EQUALS);
		mockClass.addAnnotation(an);
		Method m = createMethodForTesting(Object.class, "getPersonManager");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("job", ComparisonType.EQUALS, "MAN");inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void compositeDomainTerm() throws Exception{
		Annotation an = domainTermWithOneCondition("super manager","job", "SUPMAN", ComparisonType.EQUALS);
		mockClass.addAnnotation(an);
		Method m = createMethodForTesting(Object.class, "getPersonSuperManager");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("job", ComparisonType.EQUALS, "SUPMAN");inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void moreThanOneDomainTerm() throws Exception{
		Annotation an1 = domainTermWithOneCondition("super manager","job", "SUPMAN", ComparisonType.EQUALS);
		Annotation an2 = domainTermWithOneCondition("manager","job", "MAN", ComparisonType.EQUALS);
		Annotation an = new Annotation(DomainTerms.class, new Annotation[]{an1,an2});
		mockClass.addAnnotation(an);
		Method m = createMethodForTesting(Object.class, "getPersonManager");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("job", ComparisonType.EQUALS, "MAN");inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void domainTermWithTwoConditions() throws Exception{
		Annotation an = domainTermWithOneCondition("super manager","job", "SUPMAN", ComparisonType.EQUALS);
		Annotation t = createDomainTerm("manager");
		Annotation c1 = createCondition("job", "MAN", ComparisonType.EQUALS);
		Annotation c2 = createCondition("lastName", "S", ComparisonType.STARTS);
		t.addProperty("conditions", new Annotation[]{c1,c2});
		mockClass.addAnnotation(t);
		Method m = createMethodForTesting(Object.class, "getPersonManager");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("job", ComparisonType.EQUALS, "MAN");inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.STARTS, "S");inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void domainTermWithCondition() throws Exception{
		Annotation an = domainTermWithOneCondition("manager","job", "MAN", ComparisonType.EQUALS);
		mockClass.addAnnotation(an);
		Method m = createMethodForTesting(Object.class, "getPersonManagerByAgeGreater", int.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("job", ComparisonType.EQUALS, "MAN");inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.GREATER);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    assertEquals("Only the variable parameter should be listed",1,qi.getNamedParemeters().size());
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void domainTermWithInt() throws Exception{
		Annotation an = domainTermWithOneCondition("underage","age", "18", ComparisonType.LESSER);
		mockClass.addAnnotation(an);
		Method m = createMethodForTesting(Object.class, "getPersonUnderage");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.LESSER, 18);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void simpleOrderBy() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonOrderByName");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("name", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orderByWithCompositePropertyName() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonOrderByLastName");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("lastName", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orderByWithCompositeProperty() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonOrderByAddressCity");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("address.city", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	/*Rcontribution*/
	@Test
	public void getBySubProperty() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByAddressCity", String.class);
		QueryInfo qi = parser.parse(m);
	}
	
	@Test
	public void orderByWithAsc() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonOrderByNameAsc");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("name", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orderByWithDesc() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonOrderByNameDesc");
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("name", OrderingDirection.DESC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orderByWithParameter() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByLastNameOrderByName", String.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("name", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void orderByWithAnd() throws Exception{
		Method m = createMethodForTesting(Object.class, "getPersonByLastNameOrderByNameAndAgeDesc", String.class);
		QueryInfo qi = parser.parse(m);
	    final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("lastName", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("name", OrderingDirection.ASC);inSequence(sequence);
    	 	one(visitorMock).visitOrderBy("age", OrderingDirection.DESC);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);	
	}
	
	@Test
	public void compareToNull() throws NoSuchMethodException {
		mockClass = new ClassMock("ExampleInterface", true);
		Method m = createMethodWithAnnotationForTesting(Object.class, "getPersonByNameContains", CompareToNull.class, String.class);
		QueryInfo qi = parser.parse(m);
	    	
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.CONTAINS, NullOption.COMPARE_TO_NULL);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);
	}
	
	@Test
	public void ignoreWhenNull() throws NoSuchMethodException {
		mockClass = new ClassMock("ExampleInterface", true);
		Method m = createMethodWithAnnotationForTesting(Object.class, "getPersonByNameAndAge", IgnoreWhenNull.class, String.class, int.class);
		QueryInfo qi = parser.parse(m);
	    	
		final QueryVisitor visitorMock = context.mock(QueryVisitor.class);
	    
	    context.checking(new Expectations() {{
	    	Sequence sequence = context.sequence("query sequence");
    	 	one(visitorMock).visitEntity("Person");inSequence(sequence);
    	 	one(visitorMock).visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);inSequence(sequence);
    	 	one(visitorMock).visitConector("and");inSequence(sequence);
    	 	one(visitorMock).visitCondition("age", ComparisonType.EQUALS);inSequence(sequence);
    	 	one(visitorMock).visitEnd(); inSequence(sequence);
    	}});
	    
	    qi.visit(visitorMock);
	}
	
	@Test
	public void methodThatFitsOnParser() throws Exception{
		Method m = createMethodForTesting(List.class, "getPerson");
		assertTrue(parser.fitParserConvention(m));
	}
	
	@Test
	public void methodThatNotFitsBecauseNotStartWithGet() throws Exception{
		Method m = createMethodForTesting(List.class, "otherPerson");
		assertFalse(parser.fitParserConvention(m));
	}
	
	@Test
	public void methodThatNotFitsBecauseDontHaveAClass() throws Exception{
		Method m = createMethodForTesting(List.class, "getUnknownEntity");
		assertFalse(parser.fitParserConvention(m));
	}
	
	
	
	
	
	
	
	
	
	
	@Test
	public void queryPaginationWithPageNumberAnnotationOnly() throws Exception {
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", PageNumber.class, Integer.class);
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson is using the @PageNumber annotation but no variable or invariable page size annotation was found") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWithVariablePageSizeAnnotationOnly() throws Exception {
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", VariablePageSize.class, Integer.class);
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson is using an page size annotation but no parameter with @PageNumber was found") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWithInvariablePageSizeAnnotationOnly() throws Exception {
		ClassMock queryMockClass = new ClassMock("QueryClass");
		queryMockClass.addMethod(Person.class, "getPerson");
		
		mockClass.addAbstractMethod(Person.class, "getPerson");
		mockClass.addMethodAnnotation("getPerson", InvariablePageSize.class, 10);
		
		Class<?> c = mockClass.createClass();
		parser = createParserClass();
		parser.setInterface(c);
		
		parser.setEntityClassProvider(classProviderMock);
		final Method m = c.getMethod("getPerson");
		
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson is using an page size annotation but no parameter with @PageNumber was found") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWithTwoPageNumberAnnotations() throws Exception {
		ClassMock queryMockClass = new ClassMock("QueryClass");
		queryMockClass.addMethod(Person.class, "getPerson");
		
		mockClass.addAbstractMethod(Person.class, "getPerson", Integer.class, Integer.class);
		mockClass.addMethodAnnotation("getPerson", InvariablePageSize.class, 10);
		mockClass.addMethodParamAnnotation(0, "getPerson", PageNumber.class);
		mockClass.addMethodParamAnnotation(1, "getPerson", PageNumber.class);
		
		Class<?> c = mockClass.createClass();
		parser = createParserClass();
		parser.setInterface(c);
		
		parser.setEntityClassProvider(classProviderMock);
		final Method m = c.getMethod("getPerson", Integer.class, Integer.class);
		
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson should have only one @PageNumber annotation") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWithTwoVariablePageSizeAnnotations() throws Exception {
		ClassMock queryMockClass = new ClassMock("QueryClass");
		queryMockClass.addMethod(Person.class, "getPerson");
		
		mockClass.addAbstractMethod(Person.class, "getPerson", Integer.class, Integer.class);
		mockClass.addMethodParamAnnotation(0, "getPerson", VariablePageSize.class);
		mockClass.addMethodParamAnnotation(1, "getPerson", VariablePageSize.class);
		
		Class<?> c = mockClass.createClass();
		parser = createParserClass();
		parser.setInterface(c);
		
		parser.setEntityClassProvider(classProviderMock);
		final Method m = c.getMethod("getPerson", Integer.class, Integer.class);
		
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson should have only one page size annotation") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWrongPageNumberParameterType() throws Exception {
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", PageNumber.class, Long.class);
		new AssertException(InvalidPropertyTypeException.class, "The parameter with @PageNumber should be an integer number but is java.lang.Long") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWrongVariablePageSizeParameterType() throws Exception {
		final Method m = createMethodWithAnnotationForTesting(Person.class, "getPerson", VariablePageSize.class, Long.class);
		new AssertException(InvalidPropertyTypeException.class, "The parameter with @VariablePageSize should be an integer number but is java.lang.Long") {
			protected void run() {
				parser.parse(m);
			}
		};
	}
	
	@Test
	public void queryPaginationWithVariableInvariablePageSizeAnnotations() throws Exception {
		ClassMock queryMockClass = new ClassMock("QueryClass");
		queryMockClass.addMethod(Person.class, "getPerson");
		
		mockClass.addAbstractMethod(Person.class, "getPerson", Integer.class, Integer.class);
		mockClass.addMethodAnnotation("getPerson", InvariablePageSize.class, 10);
		mockClass.addMethodParamAnnotation(0, "getPerson", VariablePageSize.class);
		
		Class<?> c = mockClass.createClass();
		parser = createParserClass();
		parser.setInterface(c);
		
		parser.setEntityClassProvider(classProviderMock);
		final Method m = c.getMethod("getPerson", Integer.class, Integer.class);
		
		new AssertException(InvalidPaginationAnnotationSchemeException.class, "The method getPerson should have only one page size annotation") {
			protected void run() {
				parser.parse(m);
			}
		};
	}

}
