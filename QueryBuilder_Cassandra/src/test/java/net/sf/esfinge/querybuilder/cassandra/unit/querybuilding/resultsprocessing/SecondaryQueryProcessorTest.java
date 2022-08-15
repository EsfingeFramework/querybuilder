package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryUtils;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondaryQueryProcessorTest extends BasicProcessorTest {

    @Test
    public void mergeTwoQueriesResultWithDifferentElementsTest() {
        List<TestClass> secondList = new ArrayList<>();
        TestClass obj1 = new TestClass(1, "NewName", "NewLastname");
        secondList.add(obj1);

        List<TestClass> expected = new ArrayList<>();
        expected.addAll(objectList);
        expected.addAll(secondList);

        List<List<TestClass>> listOfLists = new ArrayList<>();
        listOfLists.add(objectList);
        listOfLists.add(secondList);

        ResultsProcessor processor = new SecondaryQueryProcessor();
        List<TestClass> result = processor.postProcess(SecondaryQueryUtils.fromListOfLists(listOfLists));

        assertEquals(expected, result);
    }

    @Test
    public void mergeTwoQueriesResultWithEqualElementsTest() {
        List<TestClass> secondList = new ArrayList<>();
        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(2, "Marcos", "Ferreira");
        secondList.add(obj1);
        secondList.add(obj2);

        List<TestClass> expected = new ArrayList<>(objectList);

        List<List<TestClass>> listOfLists = new ArrayList<>();
        listOfLists.add(objectList);
        listOfLists.add(secondList);

        ResultsProcessor processor = new SecondaryQueryProcessor();
        List<TestClass> result = processor.postProcess(SecondaryQueryUtils.fromListOfLists(listOfLists));

        assertEquals(expected, result);
    }

    @Test
    public void mergeThreeQueriesResultWithDifferentElementsTest() {
        List<TestClass> secondList = new ArrayList<>();
        TestClass obj1 = new TestClass(1, "NewName", "NewLastname");
        secondList.add(obj1);

        List<TestClass> thirdList = new ArrayList<>();
        TestClass obj2 = new TestClass(1, "NewName2", "NewLastname2");
        secondList.add(obj2);

        List<TestClass> expected = new ArrayList<>();
        expected.addAll(objectList);
        expected.addAll(secondList);
        expected.addAll(thirdList);

        List<List<TestClass>> listOfLists = new ArrayList<>();
        listOfLists.add(objectList);
        listOfLists.add(secondList);
        listOfLists.add(thirdList);

        ResultsProcessor processor = new SecondaryQueryProcessor();
        List<TestClass> result = processor.postProcess(SecondaryQueryUtils.fromListOfLists(listOfLists));

        assertEquals(expected, result);
    }

    @Test
    public void mergeQueriesResultWithNoResultsTest() {
        List<TestClass> secondList = new ArrayList<>();

        List<List<TestClass>> listOfLists = new ArrayList<>();
        listOfLists.add(secondList);

        ResultsProcessor processor = new SecondaryQueryProcessor();
        List<TestClass> result = processor.postProcess(SecondaryQueryUtils.fromListOfLists(listOfLists));

        assertEquals(0, result.size());
    }


}
