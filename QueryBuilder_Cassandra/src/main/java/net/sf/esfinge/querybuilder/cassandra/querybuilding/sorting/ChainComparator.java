package net.sf.esfinge.querybuilder.cassandra.querybuilding.sorting;

import java.util.Comparator;
import java.util.List;

public class ChainComparator implements Comparator<Object> {
    private List<BasicComparator> comparatorList;

    public ChainComparator(List<BasicComparator> comparatorList) {
        this.comparatorList = comparatorList;

        System.out.println("Chain length: " + comparatorList.size());
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        int result;
        for(Comparator<Object> comparator : comparatorList) {
            if ((result = comparator.compare(obj1, obj2)) != 0) {
                return result;
            }
        }
        return 0;
    }
}
