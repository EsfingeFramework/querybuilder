package org.esfinge.querybuilder.jpa1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.OrderingDirection;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

public class TestJPAQLQueryVisitor extends GenericTestJPAQLQueryVisitor {
	
	@Test
	public void singleEntity(){
		visitor.visitEntity("Person");
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o");
	}
	
	@Test
	public void oneCondition(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals");
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void conectorBeforeCondition(){
		visitor.visitEntity("Person");
		visitor.visitConector("and");
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void firstConector(){
		visitor.visitConector("and");
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void firstCondition(){
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitEnd();
	}
	
	@Test
	public void twoConditions(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitConector("and");
		visitor.visitCondition("lastName", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.lastName = :lastNameEquals");
	}

	@Test
	public void compositeCondition(){
		visitor.visitEntity("Person");
		visitor.visitCondition("address.city", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.address.city = :addressCityEquals");
	}
	
	@Test
	public void complexQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitConector("or");
		visitor.visitCondition("lastName", ComparisonType.EQUALS);
		visitor.visitConector("and");
		visitor.visitCondition("address.city", ComparisonType.EQUALS);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals or o.lastName = :lastNameEquals and o.address.city = :addressCityEquals");
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void finishWithConector(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitConector("and");
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void entityAfterStart(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitEntity("Person");
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void twoEntities(){
		visitor.visitEntity("Person");
		visitor.visitEntity("Person");
		visitor.visitEnd();
	}
	
	
	@Test
	public void differentConditionTypes(){
		testCondition(ComparisonType.GREATER, "age", "o.age > :ageGreater");
		testCondition(ComparisonType.GREATER_OR_EQUALS, "age", "o.age >= :ageGreaterOrEquals");
		testCondition(ComparisonType.LESSER, "age", "o.age < :ageLesser");
		testCondition(ComparisonType.LESSER_OR_EQUALS, "age", "o.age <= :ageLesserOrEquals");
		testCondition(ComparisonType.NOT_EQUALS, "age", "o.age <> :ageNotEquals");
	}
	
	@Test
	public void stringConditionTypes(){
		testCondition(ComparisonType.CONTAINS, "name", "o.name LIKE :nameContains");
		testCondition(ComparisonType.STARTS, "name", "o.name LIKE :nameStarts");
		testCondition(ComparisonType.ENDS, "name", "o.name LIKE :nameEnds");
	}
	
	public void testCondition(ComparisonType cp, String property, String comparison){
		QueryVisitor visitor = new JPAQLQueryVisitor();
		visitor.visitEntity("Person");
		visitor.visitCondition(property, cp);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE "+comparison);
	}
	
	@Test
	public void fixParameterQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals");
		assertEquals(qr.getFixParameterValue("nameEquals"), "Maria");
		assertTrue(qr.getFixParameters().contains("nameEquals"));
	}
	
	@Test
	public void mixedWithfixParameterQuery(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
		visitor.visitConector("and");
		visitor.visitCondition("age", ComparisonType.GREATER);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.name = :nameEquals and o.age > :ageGreater");
		assertEquals(qr.getFixParameterValue("nameEquals"), "Maria");
		assertTrue(qr.getFixParameters().contains("nameEquals"));
	}
	
	@Test
	public void mixedWithfixParameterQueryFromOtherClass(){
		visitor.visitEntity("Person");
		visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
		visitor.visitConector("and");
		visitor.visitCondition("age", ComparisonType.GREATER);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.address.state = :addressStateEquals and o.age > :ageGreater");
		assertEquals(qr.getFixParameterValue("addressStateEquals"), "SP");
		assertTrue(qr.getFixParameters().contains("addressStateEquals"));
	}
	
	@Test
	public void oneOrderBy(){
		visitor.visitEntity("Person");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o ORDER BY o.age ASC");
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void wrongOrderBy(){
		visitor.visitOrderBy("age", OrderingDirection.ASC);
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void orderByAfterConector(){
		visitor.visitEntity("Person");
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitConector("and");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void entityAfterOrderBy(){
		visitor.visitEntity("Person");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitEntity("Person");
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void conditionAfterOrderBy(){
		visitor.visitEntity("Person");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitCondition("name", ComparisonType.EQUALS);
		visitor.visitEnd();
	}
	
	@Test(expected=InvalidQuerySequenceException.class)
	public void conectorAfterOrderBy(){
		visitor.visitEntity("Person");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitConector("and");
		visitor.visitEnd();
	}
	
	@Test
	public void twoOrderBy(){
		visitor.visitEntity("Person");
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitOrderBy("name", OrderingDirection.DESC);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o ORDER BY o.age ASC, o.name DESC");
	}
	
	@Test
	public void orderByWithConditions(){
		visitor.visitEntity("Person");
		visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
		visitor.visitConector("and");
		visitor.visitCondition("age", ComparisonType.GREATER);
		visitor.visitOrderBy("age", OrderingDirection.ASC);
		visitor.visitOrderBy("name", OrderingDirection.DESC);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		String query = qr.getQuery().toString();
		assertEquals(query,"SELECT o FROM Person o WHERE o.address.state = :addressStateEquals and o.age > :ageGreater ORDER BY o.age ASC, o.name DESC");
	}

	
}
