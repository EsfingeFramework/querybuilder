package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.exceptions.ComparisonTypeNotFoundException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraSpecialComparisonQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void singleSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.NOT_EQUALS);
        visitor.visitEnd();

        SpecialComparisonClause expected = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

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

        SpecialComparisonClause expected1 = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        SpecialComparisonClause expected2 = new SpecialComparisonClause("age", SpecialComparisonType.STARTS);

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

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

        SpecialComparisonClause expected1 = new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS);
        SpecialComparisonClause expected2 = new SpecialComparisonClause("age", SpecialComparisonType.STARTS);

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

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
