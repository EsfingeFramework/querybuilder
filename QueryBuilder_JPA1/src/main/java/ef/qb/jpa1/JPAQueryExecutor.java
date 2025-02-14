package ef.qb.jpa1;

import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryStyle;
import ef.qb.core.methodparser.QueryType;
import static ef.qb.core.utils.PersistenceTypeConstants.JPA1;
import ef.qb.core.utils.ReflectionUtils;
import ef.qb.core.utils.ServiceLocator;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@QueryExecutorType(JPA1)
public class JPAQueryExecutor implements QueryExecutor {

    private static final Map<QueryInfo, QueryRepresentation> cache = new HashMap<>();

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        QueryRepresentation qr = null;
        if (cache.containsKey(info)) {
            qr = cache.get(info);
        } else {
            var visitor = JPAVisitorFactory.createQueryVisitor();
            info.visit(visitor);
            qr = visitor.getQueryRepresentation();
            cache.put(info, qr);
        }
        var q = createJPAQuery(info, args, qr);
        if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            return q.getSingleResult();
        } else {
            return q.getResultList();
        }
    }

    private EntityManager getEntityManager() {
        var emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
        var em = emp.getEntityManager();
        return em;
    }

    protected Query createJPAQuery(QueryInfo info, Object[] args, QueryRepresentation qr) {
        var em = getEntityManager();
        var query = getQuery(info, args, qr);
        var q = em.createQuery(query);
        var formatters = info.getCondition().getParameterFormatters();
        var formatterIndex = 0;
        for (var fixParam : qr.getFixParameters()) {
            q.setParameter(fixParam, formatters.get(formatterIndex).formatParameter(qr.getFixParameterValue(fixParam)));
            formatterIndex++;
        }
        if (args != null) {
            var namedParameters = info.getNamedParemeters();

            Integer number = null;
            var size = info.getPageSize();

            for (var index = 0; index < args.length; index++) {
                if (index == info.getPageNumberParameterIndex()) {
                    number = (Integer) args[index];
                } else if (index == info.getPageSizeParameterIndex()) {
                    size = (Integer) args[index];
                } else if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
                    if (args[index] != null) {
                        q.setParameter(namedParameters.get(index), formatters.get(formatterIndex).formatParameter(args[index]));
                    }

                    formatterIndex++;
                } else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {
                    var paramMap = ReflectionUtils.toParameterMap(args[index]);
                    for (var i = 0; i < namedParameters.size(); i++) {
                        var param = namedParameters.get(i);
                        var value = paramMap.get(param);
                        if (value != null) {
                            q.setParameter(param, formatters.get(formatterIndex).formatParameter(value));
                        }

                        formatterIndex++;
                    }
                }
            }

            if (size != null && number != null) {
                q.setFirstResult((number - 1) * size);
                q.setMaxResults(size);
            }
        }

        return q;
    }

    protected String getQuery(QueryInfo info, Object[] args, QueryRepresentation qr) {
        if (!qr.isDynamic()) {
            return qr.getQuery().toString();
        } else {
            Map<String, Object> params = new HashMap<>();
            var namedParameters = info.getNamedParemeters();
            if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
                for (var i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        params.put(namedParameters.get(i), args[i]);
                    } else {
                        params.put(namedParameters.get(i), null);
                    }
                }
            } else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {
                var paramMap = ReflectionUtils.toParameterMap(args[0]);
                for (var i = 0; i < namedParameters.size(); i++) {
                    var param = namedParameters.get(i);
                    var value = paramMap.get(param);
                    if (value != null) {
                        params.put(param, value);
                    } else {
                        params.put(namedParameters.get(i), null);
                    }
                }
            }
            return qr.getQuery(params).toString();
        }
    }

}
