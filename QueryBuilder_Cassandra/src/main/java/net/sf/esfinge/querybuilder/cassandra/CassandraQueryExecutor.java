package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.CassandraUtils;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.MappingManagerProvider;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtilities;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

import java.util.ArrayList;
import java.util.List;

public class CassandraQueryExecutor<E> implements QueryExecutor {

    MappingManagerProvider provider;
    private Class<E> clazz;

    public CassandraQueryExecutor() {
        provider = new MappingManagerProvider();
    }

    @Override
    public Object executeQuery(QueryInfo queryInfo, Object[] args) {
        CassandraEntityClassProvider provider = new CassandraEntityClassProvider();
        this.clazz = (Class<E>) provider.getEntityClass(queryInfo.getEntityName());
        CassandraUtils.checkValidClassConfiguration(clazz);

        QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();
        queryInfo.visit(visitor);
        QueryRepresentation qr = visitor.getQueryRepresentation();

        String query = qr.getQuery().toString();

        if (args != null) {
            query = QueryBuildingUtilities.replaceQueryArgs(query,args);
        }

        System.out.println(query);

        System.out.print("Args: ");
        if (args != null) {
            for (Object o : args)
                System.out.print(o + " ");
        }
        System.out.println();

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE){
            // return single object
        } else {
            // return list of objects
        }

        List<OrderByClause> orderByClause = ((CassandraQueryRepresentation)qr).getOrderByClause();

        return null;
    }

    public List<E> list(String query, Class<E> clazz) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);

        String keySpace = clazz.getDeclaredAnnotation(Table.class).keyspace();
        String queryWithKeyspace = query;


        ResultSet results = provider.getSession().execute(queryWithKeyspace);
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }


   /* public E getById(Object id) {
        loadManager();

        Mapper<E> mapper = manager.mapper(clazz);

        return mapper.get(id);
    }
*/
    private void printQueryInfo(QueryInfo info) {
        System.out.println("entityName: " + info.getEntityName());
        System.out.println("entityType: " + info.getEntityType().getSimpleName());
        System.out.println("queryType: " + info.getQueryType().name());
        System.out.println("*** QueryCondition ***");
        System.out.println("ParameterSize: " + info.getCondition().getParameterSize());

        System.out.print("ParameterNames: ");
        info.getCondition().getParameterNames().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("MethodParameterNames: ");
        info.getCondition().getMethodParameterNames().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("ParameterFormatters: ");
        info.getCondition().getParameterFormatters().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("MethodParameterProps: ");
        info.getCondition().getMethodParameterProps().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.println("***************************************************************");

        System.out.println("QueryStyle: " + info.getQueryStyle().name());
    }
}
