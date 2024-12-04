package ef.qb.cassandra.querybuilding.resultsprocessing.ordering;

import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetterForField;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChainComparatorFactory {

    public static ChainComparator createChainComparator(Class<?> clazz, List<OrderByClause> orderByClauses) {
        var getters = getClassGetters(clazz);

        List<Comparator> comparatorsList = new ArrayList<>();

        for (var o : orderByClauses) {
            var fieldGetter = getClassGetterForField(clazz, getters, o.getPropertyName());
            comparatorsList.add(new OrderableComparator(fieldGetter, o.getDirection()));
        }

        return new ChainComparator(comparatorsList);
    }

}
