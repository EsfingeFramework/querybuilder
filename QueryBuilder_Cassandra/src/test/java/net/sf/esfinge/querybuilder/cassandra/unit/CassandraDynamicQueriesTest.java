package net.sf.esfinge.querybuilder.cassandra.unit;

import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CassandraDynamicQueriesTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void notDynamicQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertFalse("Query should not be dynamic", qr.isDynamic());

        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE name = ? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        String query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = ? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsAndLastToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = ? ALLOW FILTERING", query);
    }

    @Test
    public void ignoreWhenNullFromVisitorWithComplexConditionsToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        String query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = ? AND name = ? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("name", "James");

        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = ? ALLOW FILTERING", query1);

        params.put("name", "James");
        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age = ? ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithComplexConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = ? AND city = ? ALLOW FILTERING", query1);

        params.put("name", "James");
        params.put("age", 30);
        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age = 30 AND city = ? ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithComparisonTypeNamingOfParametersTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", ComparisonType.LESSER_OR_EQUALS, NullOption.NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("nameEquals", null);
        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age <= ? AND city = ? ALLOW FILTERING", query1);

        params.put("nameEquals", "James");
        params.put("ageLesserOrEquals", 30);
        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age <= 30 AND city = ? ALLOW FILTERING", query2);
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void invalidCompareToNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.COMPARE_TO_NULL);
    }


    @Test
    public void twoIgnoreWhenNullQueryPlusOtherTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastname", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", 18);
        params.put("lastname", null);

        String query1 = qr.getQuery(params).toString();
        assertEquals(query1, "SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING");

        params.put("name", "James");

        String query2 = qr.getQuery(params).toString();
        assertEquals(query2, "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 ALLOW FILTERING");

        params.put("lastname", "McLoud");

        String query3 = qr.getQuery(params).toString();
        assertEquals(query3, "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 AND lastname = 'McLoud' ALLOW FILTERING");

        params.put("name", null);

        String query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 AND lastname = 'McLoud' ALLOW FILTERING", query4);
    }

    @Test
    public void threeIgnoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.GREATER_OR_EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", ComparisonType.EQUALS, NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        QueryRepresentation qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", null);
        params.put("lastName", null);

        String query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("lastName", "McLoud");

        String query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE lastName = 'McLoud' ALLOW FILTERING", query2);

        params.put("age", 18);

        String query3 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 AND lastName = 'McLoud' ALLOW FILTERING", query3);

        params.put("name", "James");

        String query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 AND lastName = 'McLoud' ALLOW FILTERING", query4);
    }
}
