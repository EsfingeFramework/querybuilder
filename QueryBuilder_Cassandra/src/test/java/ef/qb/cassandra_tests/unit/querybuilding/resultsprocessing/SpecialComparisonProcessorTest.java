package ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;

import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.CONTAINS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.ENDS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.NOT_EQUALS;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType.STARTS;
import ef.qb.cassandra_tests.unit.reflection.TestClass;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecialComparisonProcessorTest extends BasicProcessorTest {

    @Test
    public void filterListNotEqualWithOneParameterNotEqualTest() {
        List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", NOT_EQUALS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", NOT_EQUALS));
        specialComparisonClauses.get(0).setValue("Silva");

        specialComparisonClauses.add(new SpecialComparisonClause("name", NOT_EQUALS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", STARTS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", STARTS));
        specialComparisonClauses.get(0).setValue("Si");
        specialComparisonClauses.add(new SpecialComparisonClause("name", STARTS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", ENDS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", ENDS));
        specialComparisonClauses.get(0).setValue("va");
        specialComparisonClauses.add(new SpecialComparisonClause("name", ENDS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", CONTAINS));
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
        specialComparisonClauses.add(new SpecialComparisonClause("lastName", CONTAINS));
        specialComparisonClauses.get(0).setValue("ilv");
        specialComparisonClauses.add(new SpecialComparisonClause("name", CONTAINS));
        specialComparisonClauses.get(1).setValue("edr");

        ResultsProcessor processor = new SpecialComparisonProcessor(specialComparisonClauses);

        List<TestClass> expected = new ArrayList<>();
        TestClass exp1 = new TestClass(1, "Pedro", "Silva");
        expected.add(exp1);

        assertEquals(expected, processor.postProcess(objectList));
    }

}
