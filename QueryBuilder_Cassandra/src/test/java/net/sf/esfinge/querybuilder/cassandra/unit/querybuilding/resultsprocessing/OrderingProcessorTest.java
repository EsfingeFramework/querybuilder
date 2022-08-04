package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.OrderingProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class OrderingProcessorTest {

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
    public void sortListByOrderingClauseWithOneFieldAscendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 2, 4, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithOneFieldDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 1, 2, 4, 3}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsAscendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.ASC));
        orderByClauseList.add(new OrderByClause("lastName", OrderingDirection.ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 2, 4, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsSecondDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.ASC));
        orderByClauseList.add(new OrderByClause("lastName", OrderingDirection.DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 4, 2, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsAllDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.DESC));
        orderByClauseList.add(new OrderByClause("lastName", OrderingDirection.DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 1, 4, 2, 3}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithThreeFieldsTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("id", OrderingDirection.DESC));
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.ASC));
        orderByClauseList.add(new OrderByClause("lastName", OrderingDirection.DESC));

        objectList.add(new TestClass(1, "AAAAA", "AAAA"));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 4, 3, 2, 1, 1}, sorted));
    }

    @Test(expected = GetterNotFoundInClassException.class)
    public void sortListByOrderingClauseWithFieldNorFoundTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("notFoundField", OrderingDirection.ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        List<TestClass> sorted = processor.postProcess(objectList);
    }

    private boolean assertOrdering(int[] ids, List<TestClass> sorted) {
        for (int i = 0; i < sorted.size(); i++) {
            if (ids[i] != sorted.get(i).getId())
                return false;
        }

        return true;
    }
}