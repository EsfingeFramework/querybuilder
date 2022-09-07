package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering;

import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;

import java.lang.reflect.Method;
import java.util.Comparator;

public class OrderableComparator implements Comparator<Object> {

    Method compareMethod;
    OrderingDirection direction;

    public OrderableComparator(Method compareMethod, OrderingDirection direction) {
        this.compareMethod = compareMethod;
        this.direction = direction;
    }

    @Override
    public int compare(Object o1, Object o2) {
        int result;

        try {
            result = compareMethod.invoke(o1).toString().compareTo(compareMethod.invoke(o2).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return direction == OrderingDirection.ASC ? result : (-1 * result);
    }

    @Override
    public String toString() {
        return "OrderableComparator{" +
                "compareMethod=" + compareMethod.getName() +
                ", direction=" + direction.name() +
                '}';
    }
}
