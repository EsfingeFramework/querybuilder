package net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering;

import java.lang.reflect.Method;

public class NormalComparator extends BasicComparator {

    public NormalComparator(Method compareMethod) {
        super(compareMethod);
    }

    @Override
    public int compare(Object o1, Object o2) {
        System.out.println("Sorting by " + compareMethod.getName());
        int result = 0;

        try {
            result = compareMethod.invoke(o1).toString().compareTo(compareMethod.invoke(o2).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public String toString() {
        return "NormalComparator{" +
                "compareMethod=" + compareMethod.getName() +
                '}';
    }
}
