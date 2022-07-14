package net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering;

import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChainComparatorFactory {

    public static ChainComparator createChainComparator(Class<?> clazz, List<OrderByClause> orderByClauses) {
        Method[] getters = CassandraReflectionUtils.getClassGetters(clazz);

        List<Comparator> comparatorsList = new ArrayList<>();

        for (OrderByClause o : orderByClauses) {
            Method fieldGetter = CassandraReflectionUtils.getGetterForField(clazz, getters, o.propertyName);
            comparatorsList.add(new OrderableComparator(fieldGetter, o.getDirection()));
        }

        return new ChainComparator(comparatorsList);
    }

}
