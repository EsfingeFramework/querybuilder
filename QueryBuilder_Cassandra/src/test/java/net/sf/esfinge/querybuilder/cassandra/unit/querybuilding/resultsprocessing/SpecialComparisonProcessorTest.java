package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.SpecialComparisonProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecialComparisonProcessorTest {

    private List<TestClass> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(2, "Marcos", "Ferreira");
        TestClass obj3 = new TestClass(3, "Antonio", "Marques");
        TestClass obj4 = new TestClass(4, "Marcos", "Silva");
        TestClass obj5 = new TestClass(5, "Silvia", "Bressan");

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

    @Test
    public void filterListNotEqualWithOneParameterNotEqualTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.NOT_EQUALS));
        specialComparisonClauses.get(0).setValue("Silva");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(2, "Marcos", "Ferreira");
        TestClass exp2 = new TestClass(3, "Antonio", "Marques");
        TestClass exp3 = new TestClass(5, "Silvia", "Bressan");
        expected.add(exp1);
        expected.add(exp2);
        expected.add(exp3);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithTwoParametersNotEqualTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.NOT_EQUALS));
        specialComparisonClauses.get(0).setValue("Silva");

        specialComparisonClauses.add(new SpecialComparisonClause("name", SpecialComparisonType.NOT_EQUALS));
        specialComparisonClauses.get(1).setValue("Marcos");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(3, "Antonio", "Marques");
        TestClass exp2 = new TestClass(5, "Silvia", "Bressan");
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithOneParameterStartingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.STARTS));
        specialComparisonClauses.get(0).setValue("Si");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        TestClass exp2 = new TestClass(4, "Marcos", "Silva");
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithTwoParametersStartingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.STARTS));
        specialComparisonClauses.get(0).setValue("Si");
        specialComparisonClauses.add(new SpecialComparisonClause("name", SpecialComparisonType.STARTS));
        specialComparisonClauses.get(1).setValue("Pe");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        expected.add(exp1);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithOneParameterEndingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.ENDS));
        specialComparisonClauses.get(0).setValue("va");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        TestClass exp2 = new TestClass(4, "Marcos", "Silva");
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithTwoParametersEndingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.ENDS));
        specialComparisonClauses.get(0).setValue("va");
        specialComparisonClauses.add(new SpecialComparisonClause("name", SpecialComparisonType.ENDS));
        specialComparisonClauses.get(1).setValue("ro");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        expected.add(exp1);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithOneParameterContainingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.CONTAINS));
        specialComparisonClauses.get(0).setValue("ilv");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        TestClass exp2 = new TestClass(4, "Marcos", "Silva");
        expected.add(exp1);
        expected.add(exp2);

        assertEquals(expected, processor.postProcess(objectList));
    }

    @Test
    public void filterListNotEqualWithTwoParametersContainingTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", SpecialComparisonType.CONTAINS));
        specialComparisonClauses.get(0).setValue("ilv");
        specialComparisonClauses.add(new SpecialComparisonClause("name", SpecialComparisonType.CONTAINS));
        specialComparisonClauses.get(1).setValue("edr");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        expected.add(exp1);

        assertEquals(expected, processor.postProcess(objectList));
    }


}