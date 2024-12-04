package ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;

import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.GREATER_OR_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.NOT_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType.STARTS;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinProcessor;
import ef.qb.cassandra_tests.unit.reflection.TestAddress;
import ef.qb.cassandra_tests.unit.reflection.TestClassWithAddress;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoinProcessorTest {

    protected List<TestClassWithAddress> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        var obj1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        var obj2 = new TestClassWithAddress(2, "Marcos", "Ferreira", new TestAddress("Trento", "TN", 38100));
        var obj3 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        var obj4 = new TestClassWithAddress(4, "Marcos", "Silva", new TestAddress("Roma", "RM", 12345));
        var obj5 = new TestClassWithAddress(5, "Silvia", "Bressan", new TestAddress("Napoli", "NA", 54321));

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

    @Test
    public void joinListEqualWithOneParameterEqualTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", EQUALS));
        joinClauses.get(0).setValue("Bolzano");

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        var exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        var exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void joinListEqualWithTwoParametersTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", EQUALS));
        joinClauses.add(new JoinClause("address", "code", GREATER_OR_EQUALS));
        joinClauses.get(0).setValue("Bolzano");
        joinClauses.get(1).setValue(39100);

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        var exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        var exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void joinListEqualWithThreeParametersTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", STARTS));
        joinClauses.add(new JoinClause("address", "code", GREATER_OR_EQUALS));
        joinClauses.add(new JoinClause("address", "province", NOT_EQUALS));
        joinClauses.get(0).setValue("Bolz");
        joinClauses.get(1).setValue(39100);
        joinClauses.get(2).setValue("WHATEVER");

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        var exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        var exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

}
