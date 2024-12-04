package ef.qb.cassandra_tests.unit.queryvisitor;

import ef.qb.cassandra.CassandraQueryRepresentation;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import static ef.qb.cassandra.validation.CassandraVisitorFactory.createQueryVisitor;
import ef.qb.core.exception.InvalidQuerySequenceException;
import static ef.qb.core.methodparser.ComparisonType.EQUALS;
import static ef.qb.core.methodparser.OrderingDirection.ASC;
import static ef.qb.core.methodparser.OrderingDirection.DESC;
import ef.qb.core.methodparser.QueryVisitor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CassandraOrderByQueryVisitorTest {

    private final QueryVisitor visitor = createQueryVisitor();

    @Test
    public void singleOrderByTest() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", ASC);
        visitor.visitEnd();

        var expected = new OrderByClause("age", ASC);
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getOrderByClauses().get(0));
    }

    @Test
    public void doubleOrderByTest() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", ASC);
        visitor.visitOrderBy("name", DESC);
        visitor.visitEnd();

        var expected1 = new OrderByClause("age", ASC);
        var expected2 = new OrderByClause("name", DESC);
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getOrderByClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getOrderByClauses().get(1));
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void wrongOrderByTest() {
        visitor.visitOrderBy("age", ASC);
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void orderByAfterConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", EQUALS);
        visitor.visitConector("and");
        visitor.visitOrderBy("age", ASC);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterOrderByTest() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", ASC);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conditionAfterOrderByTest() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", ASC);
        visitor.visitCondition("name", EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAfterOrderByTest() {
        visitor.visitEntity("Person");
        visitor.visitOrderBy("age", ASC);
        visitor.visitConector("and");
        visitor.visitEnd();
    }
}
