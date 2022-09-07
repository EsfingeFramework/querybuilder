package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

public class CassandraQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void singleEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void visitEndBeforeEntityTest() {
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
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
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
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND city = 1? ALLOW FILTERING",
                query);
    }

    @Test
    public void threeConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND city = 1? AND age >= 2? ALLOW FILTERING",
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

    @Test(expected = InvalidQuerySequenceException.class)
    public void endAfterEndTest() {
        visitor.visitEntity("Person");
        visitor.visitEnd();
        visitor.visitEnd();
    }

    @Test
    public void fixParameterQueryWithStringValueTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Maria' ALLOW FILTERING";
        assertEquals(comparisonQuery, query);
        assertEquals("Maria", qr.getFixParameterValue("name"));
        assertTrue(qr.getFixParameters().contains("name"));
    }

    @Test
    public void fixParameterQueryWithNonStringValueTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, 30);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        String comparisonQuery = "SELECT * FROM <#keyspace-name#>.Person WHERE age = 30 ALLOW FILTERING";
        assertEquals(comparisonQuery, query);
        assertEquals(30, qr.getFixParameterValue("age"));
        assertTrue(qr.getFixParameters().contains("age"));
    }

    @Test
    public void fixParameterQueryWithNonFixParameterTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, "Maria");
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        String query = qr.getQuery().toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Maria' AND age > 0? ALLOW FILTERING", query);
        assertEquals("Maria", qr.getFixParameterValue("name"));
        assertTrue(qr.getFixParameters().contains("name"));
        assertFalse(qr.getFixParameters().contains("age"));
    }

}
