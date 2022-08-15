package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.CassandraUtils;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.MappingManagerProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderingProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraChainQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraValidationQueryVisitor;
import net.sf.esfinge.querybuilder.cassandra.validation.CassandraVisitorFactory;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<CassandraChainQueryVisitor> visitors = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitorsList();
        List<QueryRepresentation> qrList = visitors.stream().map(CassandraChainQueryVisitor::getQueryRepresentation)
                .collect(Collectors.toList());

        List<E> results = new ArrayList<>();

        for (QueryRepresentation representation : qrList) {
            String query = getQuery(queryInfo, args, representation);
            List<SpecialComparisonClause> spc = ((CassandraQueryRepresentation) representation).getSpecialComparisonClauses();
            List<SpecialComparisonClause> newSpc = SpecialComparisonUtils.getSpecialComparisonClausesWithValues(args, spc);

            ResultsProcessor specialComparisonProcessor = new SpecialComparisonProcessor(newSpc);

            List<E> queryResults = specialComparisonProcessor.postProcess(getQueryResults(query));

            results.addAll(queryResults);
        }

        if (queryInfo.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            if (results.size() > 1)
                throw new WrongTypeOfExpectedResultException("The query " + getQuery(queryInfo, args, qrList.get(0)) + " resulted in " + results.size() + " results instead of one or zero results");

            if (results.size() > 0)
                return results.get(0);
            else
                return null;
        }

        ResultsProcessor processor = new SecondaryQueryProcessor(new OrderingProcessor(((CassandraValidationQueryVisitor) visitor).getOrderByClauses()));

        return processor.postProcess(results);
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
                int argIndex = 0;

                for (int i = 0; i < args.length && argIndex < args.length; i++) {
                    if (args[argIndex] == args[i]) {
                        params.put(namedParameters.get(i), args[i]);
                        argIndex++;
                    }
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
