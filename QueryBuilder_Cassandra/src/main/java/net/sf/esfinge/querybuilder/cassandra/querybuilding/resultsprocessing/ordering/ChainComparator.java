package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering;

import java.util.Comparator;
import java.util.List;

public class ChainComparator implements Comparator<Object> {
    private final List<Comparator> comparatorList;

    public ChainComparator(List<Comparator> comparatorList) {
        this.comparatorList = comparatorList;
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        int result;
        for (Comparator<Object> comparator : comparatorList) {
            if ((result = comparator.compare(obj1, obj2)) != 0) {
                return result;
            }
        }
        return 0;
    }
}
