package net.sf.esfinge.querybuilder.cassandra.querybuilding.sorting;

import java.lang.reflect.Method;
import java.util.Comparator;

public abstract class BasicComparator implements Comparator<Object> {

    Method compareMethod;

    public BasicComparator(Method compareMethod) {
        this.compareMethod = compareMethod;
    }

    public abstract int compare(Object o1, Object o2);
}
