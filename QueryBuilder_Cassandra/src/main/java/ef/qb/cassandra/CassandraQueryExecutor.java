package ef.qb.cassandra;

import com.datastax.driver.mapping.annotations.Table;
import static ef.qb.cassandra.cassandrautils.CassandraUtils.checkValidClassConfiguration;
import ef.qb.cassandra.cassandrautils.MappingManagerProvider;
import ef.qb.cassandra.exceptions.WrongTypeOfExpectedResultException;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.replaceQueryArgs;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinUtils.getJoinClausesWithValues;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderingProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.getSpecialComparisonClausesWithValues;
import ef.qb.cassandra.validation.CassandraChainQueryVisitor;
import ef.qb.cassandra.validation.CassandraValidationQueryVisitor;
import static ef.qb.cassandra.validation.CassandraVisitorFactory.createQueryVisitor;
import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryRepresentation;
import static ef.qb.core.methodparser.QueryStyle.METHOD_SIGNATURE;
import static ef.qb.core.methodparser.QueryStyle.QUERY_OBJECT;
import static ef.qb.core.methodparser.QueryType.RETRIEVE_SINGLE;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import static ef.qb.core.utils.ReflectionUtils.toParameterMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

@QueryExecutorType(CASSANDRA)
public class CassandraQueryExecutor<E> implements QueryExecutor {

    private final MappingManagerProvider provider;
    private Class<E> clazz;

    public CassandraQueryExecutor() {
        provider = new MappingManagerProvider();
    }

    @Override
    public Object executeQuery(QueryInfo queryInfo, Object[] args) {
        var prov = new CassandraEntityClassProvider();
        this.clazz = (Class<E>) prov.getEntityClass(queryInfo.getEntityName());
        checkValidClassConfiguration(clazz);

        var visitor = createQueryVisitor();
        queryInfo.visit(visitor);

        var visitors = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitorsList();
        var qrList = visitors.stream().map(CassandraChainQueryVisitor::getQueryRepresentation)
                .collect(toList());

        List<E> results = new ArrayList<>();

        for (var representation : qrList) {
            var query = getQuery(queryInfo, args, representation);
            var spc = ((CassandraQueryRepresentation) representation).getSpecialComparisonClauses();
            var newSpc = getSpecialComparisonClausesWithValues(args, spc);
            var jcs = ((CassandraQueryRepresentation) representation).getJoinClauses();
            var newJcs = getJoinClausesWithValues(args, jcs);

            ResultsProcessor specialComparisonProcessor = new SpecialComparisonProcessor(newSpc, new JoinProcessor(newJcs));

            var queryResults = specialComparisonProcessor.postProcess(getQueryResults(query));

            results.addAll(queryResults);
        }

        if (queryInfo.getQueryType() == RETRIEVE_SINGLE) {
            if (results.size() > 1) {
                throw new WrongTypeOfExpectedResultException("The query " + getQuery(queryInfo, args, qrList.get(0)) + " resulted in " + results.size() + " results instead of one or zero results");
            }

            if (!results.isEmpty()) {
                return results.get(0);
            } else {
                return null;
            }
        }

        ResultsProcessor processor = new SecondaryQueryProcessor(new OrderingProcessor(((CassandraValidationQueryVisitor) visitor).getOrderByClauses()));

        return processor.postProcess(results);
    }

    private List<E> getQueryResults(String query) {
        var mapper = provider.getManager().mapper(clazz);
        var results = provider.getSession().execute(getQueryStringWithKeySpaceName(query));
        var objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (var u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    private String getQuery(QueryInfo queryInfo, Object[] args, QueryRepresentation qr) {
        if (!queryInfo.isDynamic() && queryInfo.getQueryStyle() != QUERY_OBJECT) {
            var query = qr.getQuery().toString();

            if (args != null) {
                query = replaceQueryArgs(query, args);
            }

            return query;
        } else {
            Map<String, Object> params = new HashMap<>();
            var namedParameters = queryInfo.getNamedParemeters();

            if (queryInfo.getQueryStyle() == METHOD_SIGNATURE) {
                var argIndex = 0;

                for (var i = 0; i < args.length && argIndex < args.length; i++) {
                    if (args[argIndex] == args[i]) {
                        params.put(namedParameters.get(i), args[i]);
                        argIndex++;
                    }
                }
            } else { // Query style is: QueryStyle.QUERY_OBJECT
                var paramMap = toParameterMap(args[0]);
                for (var key : paramMap.keySet()) {
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
