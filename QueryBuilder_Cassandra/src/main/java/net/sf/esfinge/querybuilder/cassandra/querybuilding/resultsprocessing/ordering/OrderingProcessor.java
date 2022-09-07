package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering;

import net.sf.esfinge.querybuilder.cassandra.config.ConfigReader;
import net.sf.esfinge.querybuilder.cassandra.exceptions.OrderingLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.BasicResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;

import java.util.List;
import java.util.stream.Collectors;

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
        int orderingLimit = ConfigReader.getConfiguration().getOrderingLimit();

        if (orderByClauses.isEmpty() || list.isEmpty())
            return list;

        if (list.size() > orderingLimit) {
            throw new OrderingLimitExceededException("Ordering limit has been set to " + orderingLimit + ", but the query returned " + list.size() + " results.");
        }

        Class<?> clazz = list.get(0).getClass();

        ChainComparator comparator = ChainComparatorFactory.createChainComparator(clazz, orderByClauses);

        return list.stream().sorted(comparator).collect(Collectors.toList());
    }
}
