package net.sf.esfinge.querybuilder.jdbc.worker;

import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.jdbc.JDBCQueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestJdbcQueryVistorWorker {

    private final JDBCQueryVisitor visitor = new JDBCQueryVisitor();

    @Test
    public void oneCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("Personname", ComparisonType.EQUALS);
        visitor.visitEnd();

        var query = visitor.getQuery();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.personname = 1? and person.address_id = address.id");
    }

    @Test
    public void singleEntity() {
        visitor.visitEntity("Person");
        visitor.visitEnd();
        var query = visitor.getQuery();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");
    }

    @Test(expected = InvalidQuerySequenceException.class)
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

        var query = visitor.getQuery();

        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.personname = 1? and person.lastname = 2? and person.address_id = address.id");
    }

    @Test
    public void compositeCondition() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        var query = visitor.getQuery();

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

        var query = visitor.getQuery();

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
        QueryVisitor visit = new JDBCQueryVisitor();
        visit.visitEntity("Person");
        visit.visitCondition(property, cp);
        visit.visitEnd();
        var qr = visit.getQueryRepresentation();
        var query = qr.getQuery().toString();
        var comparisonQuery = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where "
                + comparison + " and person.address_id = address.id";
        assertEquals(query.trim(), comparisonQuery.trim());
    }

    @Test
    public void fixParameterQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "maria");
        visitor.visitEnd();
        var query = visitor.getQuery();
        var comparisonQuery = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.name = 'maria' and person.address_id = address.id";
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

        var query = visitor.getQuery();
        var queryToCheck = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.name = 'maria' and person.age > 2? and person.address_id = address.id";
        assertEquals(query, queryToCheck);
        assertEquals(visitor.getFixParameterValue("nameEQUALS"), "Maria");
        assertTrue(visitor.getFixParameters().contains("nameEQUALS"));
    }

    @Test
    public void oneOrderBy() {

        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();
        var query = visitor.getQuery().trim();
        var queryToCheck = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id order by age asc";
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
        var query = visitor.getQuery();
        var queryToCompare = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id order by age asc , name desc";
        assertEquals(query, queryToCompare);
        // assertEquals(query,"SELECT o FROM Person o ORDER BY o.age ASC, o.name DESC");
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
        var query = visitor.getQuery();
        var queryToCompare = "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where address.state = 'sp' and person.age > 2? and person.address_id = address.id order by age asc , name desc";
        // "SELECT o FROM Person o WHERE o.address.state = :stateEQUALS and o.age > :age ORDER BY o.age ASC, o.name DESC";
        assertEquals(query, queryToCompare);
    }

}
