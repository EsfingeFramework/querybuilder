package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CassandraQueryVisitorTest {

    private final CassandraQueryVisitor visitor = new CassandraQueryVisitor();

    @Test
    public void singleEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitEnd();
        String query = visitor.getQuery();

        assertEquals("SELECT * FROM Person", query);
    }

    @Test(expected= InvalidQuerySequenceException.class)
    public void singleEntityWithInvalidSequenceTest() {
        visitor.visitEnd();
        visitor.visitEntity("Person");
    }

    @Test
    public void oneConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        String query = visitor.getQuery();

        assertEquals(
                "SELECT * FROM Person WHERE name = 1?",
                query);
    }

    @Test
    public void twoConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitEnd();

        String query = visitor.getQuery();

        assertEquals(
                "SELECT * FROM Person WHERE name = 1? AND city = 2?",
                query);
    }

   /* @Test(expected = InvalidQuerySequenceException.class)
    public void conectorBeforeCondition() {
        visitor.visitEntity("Person");
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void firstConector() {
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void firstCondition() {
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test
    public void twoConditions() {
        visitor.visitEntity("Person");
        visitor.visitCondition("personname", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", ComparisonType.EQUALS);
        visitor.visitEnd();

        String query = visitor.getQuery();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.personname = 1? and person.lastname = 2? and person.address_id = address.id");
    }

    @Test
    public void compositeCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        String query = visitor.getQuery();

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

        String query = visitor.getQuery();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where (person.personname = 1? or person.lastname = 2?) and address.city = 3? and person.address_id = address.id ");
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void finishWithConector() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterStart() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void twoEntities() {
        visitor.visitEntity("Person");
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test
    public void differentConditionTypes() {
        testCondition(ComparisonType.GREATER, "age", "person.age > 1?");
        testCondition(ComparisonType.GREATER_OR_EQUALS, "age",
                "person.age >= 1?");
        testCondition(ComparisonType.LESSER, "age", "person.age < 1?");
        testCondition(ComparisonType.LESSER_OR_EQUALS, "age",
                "person.age <= 1?");
        testCondition(ComparisonType.NOT_EQUALS, "age", "person.age <> 1?");
    }

    @Test
    public void stringConditionTypes() {

        testCondition(ComparisonType.CONTAINS, "name", "person.name like 1?");
        testCondition(ComparisonType.STARTS, "name", "person.name like 1?");
        testCondition(ComparisonType.ENDS, "name", "person.name like 1?");

    }

    public void testCondition(ComparisonType cp, String property,
                              String comparison) {

        QueryVisitor visitor = new JDBCQueryVisitor();
        visitor.visitEntity("Person");
        visitor.visitCondition(property, cp);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where "
                + comparison + " and person.address_id = address.id";

        assertEquals(query.trim(), comparisonQuery.trim());
    }

    @Test
    public void fixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "maria");
        visitor.visitEnd();
        String query = visitor.getQuery();
        String comparisonQuery = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.name = 'maria' and person.address_id = address.id";
        assertEquals(query, comparisonQuery);
        assertEquals(visitor.getFixParameterValue("nameEQUALS"), "maria");
        assertTrue(visitor.getFixParameters().contains("nameEQUALS"));
    }

    @Test
    public void mixedWithfixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();

        String query = visitor.getQuery();
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
        String query = visitor.getQuery();
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
        String query = visitor.getQuery();
        String queryToCompare = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where address.state = 'sp' and person.age > 2? and person.address_id = address.id order by age asc , name desc";
        assertEquals(query, queryToCompare);
    }
*/

}
