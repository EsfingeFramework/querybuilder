package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.CassandraUtils;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.MappingManagerProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
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

        if (args != null)
            query = QueryBuildingUtilities.replaceQueryArgs(query,args);

        System.out.println(query);

        List<E> results = getQueryResults(query);

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE && results.size() > 1)
            throw new WrongTypeOfExpectedResultException("The query " + query + " resulted in " + results.size() + "results");

        List<OrderByClause> orderByClause = ((CassandraQueryRepresentation)qr).getOrderByClause();
        // TODO: IMPLEMENT ORDER BY AT APPLICATION LEVEL

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE){
            if (results.size() > 0)
                return results.get(0);
            else
                return null;
        }

        return results;
    }

    public List<E> getQueryResults(String query) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);

        ResultSet results = provider.getSession().execute(getQueryStringWithKeySpaceName(query));
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    private String getQueryStringWithKeySpaceName(String query){
        return query.replace("<#keyspace-name#>",clazz.getDeclaredAnnotation(Table.class).keyspace());
    }

}
