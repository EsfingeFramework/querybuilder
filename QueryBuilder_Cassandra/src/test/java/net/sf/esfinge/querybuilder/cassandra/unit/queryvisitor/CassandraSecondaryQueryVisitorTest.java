package net.sf.esfinge.querybuilder.cassandra.unit.queryvisitor;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.exceptions.SecondaryQueryLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraValidationQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraSecondaryQueryVisitorTest {

    private final QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();

    @Test
    public void oneOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = 1? ALLOW FILTERING",
                secondaryQuery);
    }

    @Test
    public void complexOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND lastname = 1? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = 2? AND name = 3? ALLOW FILTERING",
                secondaryQuery);
    }

    @Test
    public void twoOrConnectorsTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("age", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getSecondaryVisitor().getQueryRepresentation();
        String tertiaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = 1? ALLOW FILTERING",
                secondaryQuery);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE age = 2? ALLOW FILTERING",
                tertiaryQuery);
    }

    @Test
    public void twoOrConnectorsComplexQueryTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("id", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("id", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("age", ComparisonType.EQUALS);
        visitor.visitConector("and");
        visitor.visitCondition("id", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getSecondaryVisitor().getQueryRepresentation();
        String tertiaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? AND id = 1? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE lastname = 2? AND id = 3? ALLOW FILTERING",
                secondaryQuery);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE age = 4? AND id = 5? ALLOW FILTERING",
                tertiaryQuery);
    }

    @Test(expected = SecondaryQueryLimitExceededException.class)
    public void secondaryQueryLimitExceededTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorBeforeConditionTest() {
        visitor.visitEntity("Person");
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsFirstVisitTest() {
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsLastVisitTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test(expected = InvalidQuerySequenceException.class)
    public void connectorAsLastVisitWithDoubleOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitEnd();
    }

    @Test
    public void lastSecondaryVisitorShouldBeNullWithOneOrConnectorTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.EQUALS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(null, ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getSecondaryVisitor());
    }

    @Test
    public void oneOrConnectorAndSpecialComparisonTest() {
        visitor.visitEntity("Person");
        visitor.visitCondition("name", ComparisonType.EQUALS);
        visitor.visitConector("or");
        visitor.visitCondition("lastname", ComparisonType.STARTS);
        visitor.visitEnd();

        QueryRepresentation qr = visitor.getQueryRepresentation();
        String query = qr.getQuery().toString();

        qr = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitor().getQueryRepresentation();
        String secondaryQuery = qr.getQuery().toString();

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person WHERE name = 0? ALLOW FILTERING",
                query);

        assertEquals(
                "SELECT * FROM <#keyspace-name#>.Person",
                secondaryQuery);

        SpecialComparisonClause expected = new SpecialComparisonClause("lastname", SpecialComparisonType.STARTS);

        assertEquals(expected, ((CassandraQueryRepresentation) qr).getSpecialComparisonClauses().get(0));
    }
}
