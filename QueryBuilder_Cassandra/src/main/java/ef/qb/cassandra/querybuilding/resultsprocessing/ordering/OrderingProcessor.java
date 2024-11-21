package ef.qb.cassandra.querybuilding.resultsprocessing.ordering;

import static ef.qb.cassandra.config.ConfigReader.getConfiguration;
import ef.qb.cassandra.exceptions.OrderingLimitExceededException;
import ef.qb.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.ordering.ChainComparatorFactory.createChainComparator;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class OrderingProcessor extends BasicResultsProcessor {

    private final List<OrderByClause> orderByClauses;

    public OrderingProcessor(List<OrderByClause> orderByClauses) {
        super();
        this.orderByClauses = orderByClauses;
    }

    public OrderingProcessor(List<OrderByClause> orderByClauses, ResultsProcessor nextPostprocessor) {
        super(nextPostprocessor);
        this.orderByClauses = orderByClauses;
    }

    @Override
    public <E> List<E> resultsProcessing(List<E> list) {
        int orderingLimit = getConfiguration().getOrderingLimit();

        if (orderByClauses.isEmpty() || list.isEmpty())
            return list;

        if (list.size() > orderingLimit) {
            throw new OrderingLimitExceededException("Ordering limit has been set to " + orderingLimit + ", but the query returned " + list.size() + " results.");
        }

        Class<?> clazz = list.get(0).getClass();
        ChainComparator comparator = createChainComparator(clazz, orderByClauses);

        return list.stream().sorted(comparator).collect(toList());
    }
}
