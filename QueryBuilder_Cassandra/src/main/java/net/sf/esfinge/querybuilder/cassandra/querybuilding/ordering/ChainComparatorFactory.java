package net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChainComparatorFactory {

    public static Comparator createChainComparator(List<OrderByClause> orderByClauses){
        List<Comparator> comparatorsList = new ArrayList<>();

        for (OrderByClause o : orderByClauses){



        }

        return null;
    }

    public static OrderableComparator createOrderableComparator(){


        return null;
    }
}
