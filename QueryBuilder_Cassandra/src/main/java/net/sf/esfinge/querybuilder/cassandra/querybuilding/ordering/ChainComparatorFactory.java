package net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering;

import net.sf.esfinge.querybuilder.cassandra.reflection.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChainComparatorFactory {

    public static ChainComparator createChainComparator(Class<?> clazz, List<OrderByClause> orderByClauses) {
        Method[] getters = ReflectionUtils.getClassGetters(clazz);

        List<Comparator> comparatorsList = new ArrayList<>();

        for (OrderByClause o : orderByClauses) {
            Method fieldGetter = ReflectionUtils.getGetterForField(clazz, getters, o.propertyName);
            comparatorsList.add(new OrderableComparator(fieldGetter, o.getDirection()));
        }

        return new ChainComparator(comparatorsList);
    }

}
