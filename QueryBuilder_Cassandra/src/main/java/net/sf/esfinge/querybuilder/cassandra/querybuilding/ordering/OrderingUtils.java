package net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering;

import java.util.List;
import java.util.stream.Collectors;

public class OrderingUtils {

    public static <E> List<E> sortListByOrderingClause(List<E> objectsList, List<OrderByClause> orderByClauses, Class<E> clazz) {
        ChainComparator comparator = ChainComparatorFactory.createChainComparator(clazz, orderByClauses);

        return objectsList.stream().sorted(comparator).collect(Collectors.toList());
    }
}
