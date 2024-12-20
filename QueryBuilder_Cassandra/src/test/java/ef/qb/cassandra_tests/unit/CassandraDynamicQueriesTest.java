package ef.qb.cassandra_tests.unit;

import ef.qb.cassandra.exceptions.UnsupportedCassandraOperationException;
import static ef.qb.cassandra.validation.CassandraVisitorFactory.createQueryVisitor;
import static ef.qb.core.methodparser.ComparisonType.EQUALS;
import static ef.qb.core.methodparser.ComparisonType.GREATER_OR_EQUALS;
import static ef.qb.core.methodparser.ComparisonType.LESSER_OR_EQUALS;
import ef.qb.core.methodparser.QueryVisitor;
import static ef.qb.core.methodparser.conditions.NullOption.COMPARE_TO_NULL;
import static ef.qb.core.methodparser.conditions.NullOption.IGNORE_WHEN_NULL;
import static ef.qb.core.methodparser.conditions.NullOption.NONE;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class CassandraDynamicQueriesTest {

    private final QueryVisitor visitor = createQueryVisitor();

    @Test
    public void notDynamicQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertFalse("Query should not be dynamic", qr.isDynamic());

        var query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        var query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullFromVisitorWithTwoConditionsAndLastToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 0? ALLOW FILTERING", query);
    }

    @Test
    public void ignoreWhenNullFromVisitorWithComplexConditionsToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = 0? AND name = 2? ALLOW FILTERING");
    }

    @Test
    public void ignoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? ALLOW FILTERING", query1);

        params.put("name", "James");
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age = 1? ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithComplexConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? AND city = 3? ALLOW FILTERING", query1);

        params.put("name", "James");
        params.put("age", 30);
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age = 30 AND city = 3? ALLOW FILTERING", query2);
    }

    @Test
    public void ignoreWhenNullWithComparisonTypeNamingOfParametersTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", LESSER_OR_EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("nameEquals", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age <= 1? AND city = 3? ALLOW FILTERING", query1);

        params.put("nameEquals", "James");
        params.put("ageLesserOrEquals", 30);
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age <= 30 AND city = 3? ALLOW FILTERING", query2);
    }

    @Test
    public void twoIgnoreWhenNullQueryPlusOtherTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", GREATER_OR_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastname", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", 18);
        params.put("lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(query1, "SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING");

        params.put("name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(query2, "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 ALLOW FILTERING");

        params.put("lastname", "McLoud");

        var query3 = qr.getQuery(params).toString();
        assertEquals(query3, "SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 AND lastname = 'McLoud' ALLOW FILTERING");

        params.put("name", null);

        var query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 AND lastname = 'McLoud' ALLOW FILTERING", query4);
    }

    @Test
    public void threeIgnoreWhenNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", GREATER_OR_EQUALS, IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", EQUALS, IGNORE_WHEN_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", null);
        params.put("lastName", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("lastName", "McLoud");

        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE lastName = 'McLoud' ALLOW FILTERING", query2);

        params.put("age", 18);

        var query3 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 AND lastName = 'McLoud' ALLOW FILTERING", query3);

        params.put("name", "James");

        var query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE name = 'James' AND age >= 18 AND lastName = 'McLoud' ALLOW FILTERING", query4);
    }

    @Test
    public void compareToNullTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        var query = qr.getQuery().toString();
        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void invalidCompareToNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", GREATER_OR_EQUALS, COMPARE_TO_NULL);
    }

    @Test
    public void compareToNullFromVisitorWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? ALLOW FILTERING");
    }

    @Test
    public void compareToNullFromVisitorWithTwoConditionsAndLastToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 0? ALLOW FILTERING", query);
    }

    @Test
    public void compareToNullFromVisitorWithComplexConditionsToBeIgnoredTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("name", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person WHERE age = 0? AND name = 2? ALLOW FILTERING");
    }

    @Test
    public void compareToNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query2);
    }

    @Test
    public void compareToNullWithTwoConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? ALLOW FILTERING", query1);

        params.put("name", "James");
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? ALLOW FILTERING", query2);
    }

    @Test
    public void compareToNullWithComplexConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("name", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 1? AND city = 3? ALLOW FILTERING", query1);

        params.put("name", "James");
        params.put("age", 30);
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age = 30 AND city = 3? ALLOW FILTERING", query2);
    }

    @Test
    public void compareToNullWithComparisonTypeNamingOfParametersTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("age", LESSER_OR_EQUALS, NONE);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("AND");
        visitor.visitCondition("city", EQUALS, NONE);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();
        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();

        params.put("nameEquals", null);
        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age <= 1? AND city = 3? ALLOW FILTERING", query1);

        params.put("nameEquals", "James");
        params.put("ageLesserOrEquals", 30);
        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age <= 30 AND city = 3? ALLOW FILTERING", query2);
    }

    @Test
    public void twoCompareToNullQueryPlusOtherTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", GREATER_OR_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastname", EQUALS, COMPARE_TO_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", 18);
        params.put("lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(query1, "SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING");

        params.put("name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(query2, "SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING");

        params.put("lastname", "McLoud");

        var query3 = qr.getQuery(params).toString();
        assertEquals(query3, "SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING");

        params.put("name", null);

        var query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE age >= 18 ALLOW FILTERING", query4);
    }

    @Test
    public void threeCompareToNullQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("age", EQUALS, COMPARE_TO_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", EQUALS, COMPARE_TO_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("age", null);
        params.put("lastName", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query1);

        params.put("lastName", "McLoud");

        var query2 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query2);

        params.put("age", 18);

        var query3 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query3);

        params.put("name", "James");

        var query4 = qr.getQuery(params).toString();
        assertEquals("SELECT * FROM <#keyspace-name#>.Person", query4);
    }

}
