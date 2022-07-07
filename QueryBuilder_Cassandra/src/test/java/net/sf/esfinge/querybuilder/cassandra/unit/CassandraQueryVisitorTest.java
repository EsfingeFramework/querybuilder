package net.sf.esfinge.querybuilder.cassandra.unit;

import net.sf.esfinge.querybuilder.cassandra.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void singleEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals("SELECT * FROM Person", query);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void singleEntityWithInvalidSequenceTest() {
        visitor.visitEnd();
        visitor.visitEntity("Person");
    }

    @Test
    public void oneConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM Person WHERE name = ?",
                query);
    }

    @Test
    public void twoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM Person WHERE name = ? AND city = ?",
                query);
    }

    @Test
    public void threeConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitConector("OR");
        visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM Person WHERE name = ? AND city = ? OR age >= ?",
                query);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void twoConditionsWithNoConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidConnectorException.class)
    public void invalidConnectorNameTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("wrongConnector");
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorBeforeConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsFirstVisitTest() {
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conditionAsFirstVisitTest() {
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsLastVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterConditionVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void twoConsecutiveEntitiesTest() {
        visitor.visitEntity("Person");
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    /*

     TODO: PROBLEM WITH COMPLEX QUERIES AND CASSANDRA: JOINS DO NOT EXIST, IMPLEMENT THEM AT APPLICATION LEVEL??

    @Test
    public void compositeCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where address.city = 1? and person.address_id = address.id");
    }

    @Test
    public void complexQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("personname", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastName", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where (person.personname = 1? or person.lastname = 2?) and address.city = 3? and person.address_id = address.id ");
    }*/

    /*@Test
    public void differentConditionTypes() {
        testCondition(ComparisonType.GREATER, "age", "person.age > 1?");
        testCondition(ComparisonType.GREATER_OR_EQUALS, "age",
                "person.age >= 1?");
        testCondition(ComparisonType.LESSER, "age", "person.age < 1?");
        testCondition(ComparisonType.LESSER_OR_EQUALS, "age",
                "person.age <= 1?");
        testCondition(ComparisonType.NOT_EQUALS, "age", "person.age <> 1?");
    }

    /*@Test
    public void stringConditionTypes() {

        testCondition(ComparisonType.CONTAINS, "name", "person.name like 1?");
        testCondition(ComparisonType.STARTS, "name", "person.name like 1?");
        testCondition(ComparisonType.ENDS, "name", "person.name like 1?");

    }*/

    /*public void testCondition(ComparisonType cp, String property,
                              String comparison) {

        QueryVisitor visitor = new CassandraQueryVisitor();
        visitor.visitEntity("Person");
        visitor.visitCondition(property, cp);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where "
                + comparison + " and person.address_id = address.id";

        assertEquals(query.trim(), comparisonQuery.trim());
    }*/

    @Test
    public void fixParameterQueryWithStringValueTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "maria");
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "SELECT * FROM Person WHERE name = 'maria'";
        assertEquals(comparisonQuery, query);
    }

    @Test
    public void fixParameterQueryWithNonStringValueTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, 30);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "SELECT * FROM Person WHERE age = 30";
        assertEquals(comparisonQuery, query);
    }

    /*@Test
    public void mixedWithfixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        String queryToCheck = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.name = 'maria' and person.age > 2? and person.address_id = address.id";
        assertEquals(query, queryToCheck);
        assertEquals(visitor.getFixParameterValue("nameEQUALS"), "Maria");
        assertTrue(visitor.getFixParameters().contains("nameEQUALS"));
    }

    @Test
    public void oneOrderBy() {

        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();
        String query = visitor.getQuery().trim();
        String queryToCheck = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id order by age asc";
        assertEquals(query, queryToCheck);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void wrongOrderBy() {
        visitor.visitOrderBy("age", OrderingDirection.ASC);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void orderByAfterConector() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conditionAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conectorAfterOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test
    public void twoOrderBy() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitOrderBy("name", OrderingDirection.DESC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        String queryToCompare = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id order by age asc , name desc";
        assertEquals(query, queryToCompare);
    }

    @Test
    public void orderByWithConditions() {

        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS, "SP");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitOrderBy("name", OrderingDirection.DESC);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();
        String queryToCompare = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where address.state = 'sp' and person.age > 2? and person.address_id = address.id order by age asc , name desc";
        assertEquals(query, queryToCompare);
    }
*/

}
