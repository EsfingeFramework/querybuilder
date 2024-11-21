package ef.qb.cassandra_tests.unit.queryvisitor;

import ef.qb.cassandra.CassandraQueryRepresentation;
import ef.qb.cassandra.exceptions.JoinDepthLimitExceededException;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import ef.qb.cassandra.validation.CassandraVisitorFactory;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryVisitor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraJoinQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void oneJoinConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected = new JoinClause("address", "state", JoinComparisonType.EQUALS);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person",
                query);
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));

    }

    @Test
    public void twoJoinConditionsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected1 = new JoinClause("address", "state", JoinComparisonType.EQUALS);
        JoinClause expected2 = new JoinClause("address", "city", JoinComparisonType.EQUALS);
        expected2.setArgPosition(1);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person",
                query);
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getJoinClauses().get(1));

    }

    @Test
    public void oneConditionForMainEntityAndOneJoinTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected = new JoinClause("address", "state", JoinComparisonType.EQUALS);
        expected.setArgPosition(1);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
                query);
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));

    }

    @Test
    public void twoConditionsForMainEntityAndOneJoinTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected = new JoinClause("address", "state", JoinComparisonType.EQUALS);
        expected.setArgPosition(2);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND lastname = 1? ALLOW FILTERING",
                query);
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
    }

    @Test
    public void oneJoinAndOneConditionForMainEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected = new JoinClause("address", "state", JoinComparisonType.EQUALS);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 1? ALLOW FILTERING",
                query);

        assertEquals(expected, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
    }

    @Test
    public void twoJoinsAndOneConditionForMainEntityTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("address.city", ComparisonType.EQUALS);
        visitor.visitConector("AND");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        JoinClause expected1 = new JoinClause("address", "state", JoinComparisonType.EQUALS);
        JoinClause expected2 = new JoinClause("address", "city", JoinComparisonType.EQUALS);
        expected2.setArgPosition(1);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 2? ALLOW FILTERING",
                query);
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getJoinClauses().get(1));
    }

    @Test
    public void oneJoinConditionAndOneOrderByClauseTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitOrderBy("age", OrderingDirection.ASC);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        OrderByClause expectedOrderBy = new OrderByClause("age", OrderingDirection.ASC);
        JoinClause expectedJoin = new JoinClause("address", "state", JoinComparisonType.EQUALS);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person",
                query);
        assertEquals(expectedOrderBy, ((CassandraQueryRepresentation) visitor.getQueryRepresentation()).getOrderByClauses().get(0));
        assertEquals(expectedJoin, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
    }

    @Test
    public void oneJoinConditionAndOneSpecialClauseTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("address.state", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        SpecialComparisonClause expectedSpecialClause = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);

        JoinClause expectedJoin = new JoinClause("address", "state", JoinComparisonType.EQUALS);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person",
                query);

        assertEquals(expectedSpecialClause, ((CassandraQueryRepresentation) visitor.getQueryRepresentation()).getSpecialComparisonClauses().get(0));
        assertEquals(expectedJoin, ((CassandraQueryRepresentation) qr).getJoinClauses().get(0));
    }

    @Test
    public void joinDepthExceededTest() {
        visitor.visitEntity("Person");
        assertThrows(JoinDepthLimitExceededException.class, () -> visitor.visitCondition("address.state.name", ComparisonType.EQUALS));
    }

}
