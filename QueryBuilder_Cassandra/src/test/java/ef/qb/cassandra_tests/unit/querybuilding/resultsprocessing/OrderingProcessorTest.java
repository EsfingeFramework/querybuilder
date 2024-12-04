package ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;

import ef.qb.cassandra.exceptions.GetterNotFoundInClassException;
import ef.qb.cassandra.exceptions.OrderingLimitExceededException;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderingProcessor;
import ef.qb.cassandra_tests.unit.reflection.TestClass;
import static ef.qb.core.methodparser.OrderingDirection.ASC;
import static ef.qb.core.methodparser.OrderingDirection.DESC;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderingProcessorTest extends BasicProcessorTest {

    @Test
    public void sortListByOrderingClauseWithOneFieldAscendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 2, 4, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithOneFieldDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 1, 2, 4, 3}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsAscendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", ASC));
        orderByClauseList.add(new OrderByClause("lastName", ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 2, 4, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsSecondDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", ASC));
        orderByClauseList.add(new OrderByClause("lastName", DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{3, 4, 2, 1, 5}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithTwoFieldsAllDescendingTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", DESC));
        orderByClauseList.add(new OrderByClause("lastName", DESC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 1, 4, 2, 3}, sorted));
    }

    @Test
    public void sortListByOrderingClauseWithThreeFieldsTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("id", DESC));
        orderByClauseList.add(new OrderByClause("name", ASC));
        orderByClauseList.add(new OrderByClause("lastName", DESC));

        objectList.add(new TestClass(1, "AAAAA", "AAAA"));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);

        assertTrue(assertOrdering(new int[]{5, 4, 3, 2, 1, 1}, sorted));
    }

    @Test(expected = GetterNotFoundInClassException.class)
    public void sortListByOrderingClauseWithFieldNorFoundTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("notFoundField", ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);
        var sorted = processor.postProcess(objectList);
    }

    private boolean assertOrdering(int[] ids, List<TestClass> sorted) {
        for (var i = 0; i < sorted.size(); i++) {
            if (ids[i] != sorted.get(i).getId()) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void sortListWithOrderingLimitExceededTest() {
        List<OrderByClause> orderByClauseList = new ArrayList<>();
        orderByClauseList.add(new OrderByClause("name", ASC));

        ResultsProcessor processor = new OrderingProcessor(orderByClauseList);

        for (var i = 0; i < 6; i++) {
            objectList.add(new TestClass(1, "test", "test"));
        }

        assertThrows(OrderingLimitExceededException.class, () -> processor.postProcess(objectList));
    }
}
