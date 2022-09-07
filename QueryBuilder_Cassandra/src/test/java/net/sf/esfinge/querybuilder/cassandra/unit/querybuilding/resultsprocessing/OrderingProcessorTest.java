package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.exceptions.GetterNotFoundInClassException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.OrderingLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderingProcessor;
import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderingProcessorTest extends BasicProcessorTest {

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

    @Test
    public void sortListWithOrderingLimitExceededTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", OrderingDirection.ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);

        for (int i = 0; i < 6; i++)
            objectList.add(new TestClass(1, "test", "test"));

        assertThrows(OrderingLimitExceededException.class, () -> processor.postProcess(objectList));
    }
}