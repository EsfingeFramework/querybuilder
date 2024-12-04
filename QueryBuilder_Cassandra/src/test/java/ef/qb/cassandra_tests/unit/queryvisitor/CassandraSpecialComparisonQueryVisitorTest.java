package ef.qb.cassandra_tests.unit.queryvisitor;

import ef.qb.cassandra.CassandraQueryRepresentation;
import ef.qb.cassandra.exceptions.ComparisonTypeNotFoundException;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import ef.qb.cassandra.validation.CassandraVisitorFactory;
import ef.qb.core.exception.InvalidQuerySequenceException;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.QueryVisitor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraSpecialComparisonQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void singleSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEnd();

        var expected = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
    }

    @Test
    public void multipleSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.STARTS);
        visitor.visitEnd();

        var expected1 = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        var expected2 = new SpecialComparisonClause("age", SpecialComparisonType.STARTS);
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals(query, "SELECT * FROM <#keyspace-name#>.Person");
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(1));
    }

    @Test
    public void specialComparisonMixedWithNormalComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastName", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("age", ComparisonType.STARTS);
        visitor.visitEnd();

        var expected1 = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        var expected2 = new SpecialComparisonClause("age", SpecialComparisonType.STARTS);
        var qr = visitor.getQueryRepresentation();
        var query = qr.getQuery().toString();

        assertEquals("SELECT * FROM <#keyspace-name#>.Person WHERE lastName = 1? ALLOW FILTERING", query);
        assertEquals(expected1, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
        assertEquals(expected2, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(1));
    }

    @Test
    public void comparisonTypeNotFoundTest() {
        assertThrows(ComparisonTypeNotFoundException.class, () -> {
            SpecialComparisonType.fromComparisonType(ComparisonType.EQUALS);
        });
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void entityAfterSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEntity("Person");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void conditionAfterSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAfterSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitConector("and");
        visitor.visitEnd();
    }

}
