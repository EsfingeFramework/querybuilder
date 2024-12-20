package ef.qb.cassandra_tests.unit.queryvisitor;

import ef.qb.cassandra.exceptions.InvalidConnectorException;
import static ef.qb.cassandra.validation.CassandraVisitorFactory.createQueryVisitor;
import ef.qb.core.exception.InvalidQuerySequenceException;
import static ef.qb.core.methodparser.ComparisonType.EQUALS;
import static ef.qb.core.methodparser.ComparisonType.GREATER;
import static ef.qb.core.methodparser.ComparisonType.GREATER_OR_EQUALS;
import ef.qb.core.methodparser.QueryVisitor;
import static org.junit.Assert.*;
import org.junit.Test;

public class CassandraQueryVisitorTest {

    private final QueryVisitor visitor = createQueryVisitor();

    @Test
    public void singleEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

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
        visitor.visitCondition("name", EQUALS);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
                query);
    }

    @Test
    public void twoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND city = 1? ALLOW FILTERING",
                query);
    }

    @Test
    public void threeConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("age", GREATER_OR_EQUALS);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND city = 1? AND age >= 2? ALLOW FILTERING",
                query);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void twoConditionsWithNoConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitCondition("city", EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidConnectorException.class)
    public void invalidConnectorNameTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitConector("wrongConnector");
        visitor.visitCondition("city", EQUALS);
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
        visitor.visitCondition("name", EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsLastVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterConditionVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
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
        visitor.visitCondition("name", EQUALS, "Maria");
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();
        var comparisonQuery = "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Maria' ALLOW FILTERING";
        assertEquals(comparisonQuery, query);
        assertEquals("Maria", qr.getFixParameterValue("name"));
        assertTrue(qr.getFixParameters().contains("name"));
    }

    @Test
    public void fixParameterQueryWithNonStringValueTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", EQUALS, 30);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();
        var comparisonQuery = "SELECT * FROM <#keyspace-name#>.Person WHERE age = 30 ALLOW FILTERING";
        assertEquals(comparisonQuery, query);
        assertEquals(30, qr.getFixParameterValue("age"));
        assertTrue(qr.getFixParameters().contains("age"));
    }

    @Test
    public void fixParameterQueryWithNonFixParameterTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, "Maria");
        visitor.visitConector("and");
        visitor.visitCondition("age", GREATER);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'Maria' AND age > 0? ALLOW FILTERING", query);
        assertEquals("Maria", qr.getFixParameterValue("name"));
        assertTrue(qr.getFixParameters().contains("name"));
        assertFalse(qr.getFixParameters().contains("age"));
    }

}
