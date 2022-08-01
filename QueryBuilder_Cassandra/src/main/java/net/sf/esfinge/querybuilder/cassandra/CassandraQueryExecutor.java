package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.CassandraUtils;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.MappingManagerProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ordering.OrderingUtils;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String query = getQuery(queryInfo, args, qr);

        List<E> results = getQueryResults(query);

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE && results.size() > 1)
            throw new WrongTypeOfExpectedResultException("The query " + query + " resulted in " + results.size() + "results");

        List<OrderByClause> orderByClause = ((CassandraQueryRepresentation) qr).getOrderByClause();

        if (!orderByClause.isEmpty()) {
            results = OrderingUtils.sortListByOrderingClause(results, orderByClause, clazz);
        }

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            if (results.size() > 0)
                return results.get(0);
            else
                return null;
        }

        return results;
    }

    private List<E> getQueryResults(String query) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);

        ResultSet results = provider.getSession().execute(getQueryStringWithKeySpaceName(query));
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    private String getQuery(QueryInfo queryInfo, Object[] args, QueryRepresentation qr) {
        if (!queryInfo.isDynamic() && queryInfo.getQueryStyle() != QueryStyle.QUERY_OBJECT) {
            String query = qr.getQuery().toString();

            if (args != null)
                query = QueryBuildingUtils.replaceQueryArgs(query, args);

            return query;
        } else {
            Map<String, Object> params = new HashMap<>();
            List<String> namedParameters = queryInfo.getNamedParemeters();
            if (queryInfo.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
                for (int i = 0; i < args.length; i++) {
                    params.put(namedParameters.get(i), args[i]);
                }
            } else { // Query style is: QueryStyle.QUERY_OBJECT
                Map<String, Object> paramMap = ReflectionUtils.toParameterMap(args[0]);
                for (String key : paramMap.keySet()) {
                    params.put(key, paramMap.get(key));
                }
            }

            return qr.getQuery(params).toString();
        }
    }

    private String getQueryStringWithKeySpaceName(String query) {
        return query.replace("<#keyspace-name#>", clazz.getDeclaredAnnotation(Table.class).keyspace());
    }

}
