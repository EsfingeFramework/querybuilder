package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

import java.util.List;

public class CassandraQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo queryInfo, Object[] objects) {
        System.out.println("Executing query...");

        System.out.println("Query info");
        System.out.println(queryInfo.getEntityName());
        System.out.println(queryInfo.getQueryType().name());

        for (Object o : objects)
            System.out.println(o);

        return null;
    }
}
