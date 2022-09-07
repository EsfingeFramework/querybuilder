package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinProcessor;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestAddress;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClassWithAddress;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoinProcessorTest {

    protected List<TestClassWithAddress> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        TestClassWithAddress obj1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        TestClassWithAddress obj2 = new TestClassWithAddress(2, "Marcos", "Ferreira", new TestAddress("Trento", "TN", 38100));
        TestClassWithAddress obj3 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        TestClassWithAddress obj4 = new TestClassWithAddress(4, "Marcos", "Silva", new TestAddress("Roma", "RM", 12345));
        TestClassWithAddress obj5 = new TestClassWithAddress(5, "Silvia", "Bressan", new TestAddress("Napoli", "NA", 54321));

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

    @Test
    public void joinListEqualWithOneParameterEqualTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", JoinComparisonType.EQUALS));
        joinClauses.get(0).setValue("Bolzano");

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        TestClassWithAddress exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        TestClassWithAddress exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void joinListEqualWithTwoParametersTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", JoinComparisonType.EQUALS));
        joinClauses.add(new JoinClause("address", "code", JoinComparisonType.GREATER_OR_EQUALS));
        joinClauses.get(0).setValue("Bolzano");
        joinClauses.get(1).setValue(39100);

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        TestClassWithAddress exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        TestClassWithAddress exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void joinListEqualWithThreeParametersTest() {
        List<JoinClause> joinClauses = new ArrayList<>();
        joinClauses.add(new JoinClause("address", "city", JoinComparisonType.STARTS));
        joinClauses.add(new JoinClause("address", "code", JoinComparisonType.GREATER_OR_EQUALS));
        joinClauses.add(new JoinClause("address", "province", JoinComparisonType.NOT_EQUALS));
        joinClauses.get(0).setValue("Bolz");
        joinClauses.get(1).setValue(39100);
        joinClauses.get(2).setValue("WHATEVER");

        ResultsProcessor processor = new JoinProcessor(joinClauses);

        List<TestClassWithAddress> expected = new ArrayList<>();
        TestClassWithAddress exp1 = new TestClassWithAddress(1, "Pedro", "Silva", new TestAddress("Bolzano", "BZ", 39100));
        TestClassWithAddress exp2 = new TestClassWithAddress(3, "Antonio", "Marques", new TestAddress("Bolzano", "BZ", 39100));
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

}
