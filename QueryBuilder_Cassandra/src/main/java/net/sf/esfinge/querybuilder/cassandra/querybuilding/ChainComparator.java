package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import java.util.Comparator;
import java.util.List;

class ChainComparator implements Comparator<Object> {
    private List<Comparator<Object>> comparatorList;

    public ChainComparator(List<Comparator<Object>> comparatorList) {
        this.comparatorList = comparatorList;
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        int result;
        for(Comparator<Object> comparator : comparatorList) {
            if ((result = comparator.compare(obj1, obj1)) != 0) {
                return result;
            }
        }
        return 0;
    }
}
