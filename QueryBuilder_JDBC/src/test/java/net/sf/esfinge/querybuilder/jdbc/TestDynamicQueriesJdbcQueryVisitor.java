package net.sf.esfinge.querybuilder.jdbc;

import java.util.HashMap;
import java.util.Map;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestDynamicQueriesJdbcQueryVisitor {

    private final JDBCQueryVisitor visitor = new JDBCQueryVisitor();

    @Test
    public void notDynamicQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        assertFalse("Query should not be dynamic", qr.isDynamic());
        var query = qr.getQuery().toString();
        assertEquals(
                query,
                "select person.id, person.name, person.lastname, person.age, address.id, address.city, address.state from person, address where person.name = 1? and person.address_id = address.id");
    }

    @Test
    public void ignoreWhenNullQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");

        params.put("person.name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.address_id = address.id");
    }

    @Test
    public void compareToNullQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.COMPARE_TO_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();

        assertTrue("Query should be dynamic", qr.isDynamic());

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = null and person.address_id = address.id");

        params.put("person.name", "James");
        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.address_id = address.id");
    }

    @Test
    public void twoCompareToNullQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.COMPARE_TO_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("person.lastname", ComparisonType.EQUALS,
                NullOption.COMPARE_TO_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);
        params.put("person.lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = null and person.lastname = null and person.address_id = address.id");

        params.put("person.name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.lastname = null and person.address_id = address.id");

        params.put("person.lastname", "McLoud");

        var query3 = qr.getQuery(params).toString();
        assertEquals(
                query3,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.lastname = 'McLoud' and person.address_id = address.id");

        params.put("person.name", null);

        var query4 = qr.getQuery(params).toString();
        assertEquals(
                query4,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = null and person.lastname = 'McLoud' and person.address_id = address.id");
    }

    @Test
    public void twoIgnoreWhenNullQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("person.lastname", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);
        params.put("person.lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");

        params.put("person.name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.address_id = address.id");

        params.put("person.lastname", "McLoud");

        var query3 = qr.getQuery(params).toString();
        assertEquals(
                query3,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.lastname = 'McLoud' and person.address_id = address.id");

        params.put("person.name", null);

        var query4 = qr.getQuery(params).toString();
        assertEquals(
                query4,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.lastname = 'McLoud' and person.address_id = address.id");
    }

    @Test
    public void twoIgnoreWhenNullQueryPlusOther() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("person.age", ComparisonType.GREATER_OR_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("person.lastname", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();
        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);
        params.put("person.age", 18);
        params.put("person.lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.age >= 18 and person.address_id = address.id");

        params.put("person.name", "James");

        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.age >= 18 and person.address_id = address.id");

        params.put("person.lastname", "McLoud");

        var query3 = qr.getQuery(params).toString();
        assertEquals(
                query3,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.age >= 18 and person.lastname = 'McLoud' and person.address_id = address.id");

        params.put("person.name", null);

        var query4 = qr.getQuery(params).toString();
        assertEquals(
                query4,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.age >= 18 and person.lastname = 'McLoud' and person.address_id = address.id");
    }

    @Test
    public void threeIgnoreWhenNullQuery() {
        visitor.visitEntity("Person");
        visitor.visitCondition("person.name", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("person.age", ComparisonType.GREATER_OR_EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitConector("and");
        visitor.visitCondition("person.lastname", ComparisonType.EQUALS,
                NullOption.IGNORE_WHEN_NULL);
        visitor.visitEnd();

        var qr = visitor.getQueryRepresentation();

        Map<String, Object> params = new HashMap<>();
        params.put("person.name", null);
        params.put("person.age", null);
        params.put("person.lastname", null);

        var query1 = qr.getQuery(params).toString();
        assertEquals(
                query1,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.address_id = address.id");

        params.put("person.lastname", "McLoud");

        var query2 = qr.getQuery(params).toString();
        assertEquals(
                query2,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.lastname = 'McLoud' and person.address_id = address.id");

        params.put("person.age", 18);

        var query3 = qr.getQuery(params).toString();
        assertEquals(
                query3,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.age >= 18 and person.lastname = 'McLoud' and person.address_id = address.id");

        params.put("person.name", "James");

        var query4 = qr.getQuery(params).toString();
        assertEquals(
                query4,
                "select person.id, person.name, person.lastName, person.age, address.id, address.city, address.state from person, address where person.name = 'James' and person.age >= 18 and person.lastname = 'McLoud' and person.address_id = address.id");
    }

}
