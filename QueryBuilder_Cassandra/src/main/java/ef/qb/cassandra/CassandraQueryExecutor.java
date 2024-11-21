package ef.qb.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import static ef.qb.cassandra.cassandrautils.CassandraUtils.checkValidClassConfiguration;
import ef.qb.cassandra.cassandrautils.MappingManagerProvider;
import ef.qb.cassandra.exceptions.WrongTypeOfExpectedResultException;
import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.replaceQueryArgs;
import ef.qb.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinProcessor;
import static ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinUtils.getJoinClausesWithValues;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderingProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery.SecondaryQueryProcessor;
import ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
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
import ef.qb.core.methodparser.QueryVisitor;
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
        CassandraEntityClassProvider prov = new CassandraEntityClassProvider();
        this.clazz = (Class<E>) prov.getEntityClass(queryInfo.getEntityName());
        checkValidClassConfiguration(clazz);

        QueryVisitor visitor = createQueryVisitor();
        queryInfo.visit(visitor);

        List<CassandraChainQueryVisitor> visitors = ((CassandraValidationQueryVisitor) visitor).getSecondaryVisitorsList();
        List<QueryRepresentation> qrList = visitors.stream().map(CassandraChainQueryVisitor::getQueryRepresentation)
                .collect(toList());

        List<E> results = new ArrayList<>();

        for (QueryRepresentation representation : qrList) {
            String query = getQuery(queryInfo, args, representation);
            List<SpecialComparisonClause> spc = ((CassandraQueryRepresentation) representation).getSpecialComparisonClauses();
            List<SpecialComparisonClause> newSpc = getSpecialComparisonClausesWithValues(args, spc);
            List<JoinClause> jcs = ((CassandraQueryRepresentation) representation).getJoinClauses();
            List<JoinClause> newJcs = getJoinClausesWithValues(args, jcs);

            ResultsProcessor specialComparisonProcessor = new SpecialComparisonProcessor(newSpc, new JoinProcessor(newJcs));

            List<E> queryResults = specialComparisonProcessor.postProcess(getQueryResults(query));

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
        if (!queryInfo.isDynamic() && queryInfo.getQueryStyle() != QUERY_OBJECT) {
            String query = qr.getQuery().toString();

            if (args != null) {
                query = replaceQueryArgs(query, args);
            }

            return query;
        } else {
            Map<String, Object> params = new HashMap<>();
            List<String> namedParameters = queryInfo.getNamedParemeters();

            if (queryInfo.getQueryStyle() == METHOD_SIGNATURE) {
                int argIndex = 0;

                for (int i = 0; i < args.length && argIndex < args.length; i++) {
                    if (args[argIndex] == args[i]) {
                        params.put(namedParameters.get(i), args[i]);
                        argIndex++;
                    }
                }
            } else { // Query style is: QueryStyle.QUERY_OBJECT
                Map<String, Object> paramMap = toParameterMap(args[0]);
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
