package net.sf.esfinge.querybuilder.jpa1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryStyle;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JPAQueryExecutor implements QueryExecutor {

	private static Map<QueryInfo, QueryRepresentation> cache = new HashMap<QueryInfo, QueryRepresentation>();

	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		QueryRepresentation qr = null;
		if (cache.containsKey(info)) {
			qr = cache.get(info);
		} else {
			QueryVisitor visitor = JPAVisitorFactory.createQueryVisitor();
			info.visit(visitor);
			qr = visitor.getQueryRepresentation();
			cache.put(info, qr);
		}
		Query q = createJPAQuery(info, args, qr);
		if (info.getQueryType() == QueryType.RETRIEVE_SINGLE)
			return q.getSingleResult();
		else
			return q.getResultList();
	}

	private EntityManager getEntityManager() {
		EntityManagerProvider emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
		EntityManager em = emp.getEntityManager();
		return em;
	}

	protected Query createJPAQuery(QueryInfo info, Object[] args, QueryRepresentation qr) {
		EntityManager em = getEntityManager();
		String query = getQuery(info, args, qr);
		Query q = em.createQuery(query);
		List<ParameterFormater> formatters = info.getCondition().getParameterFormatters();
		int formatterIndex = 0;
		for (String fixParam : qr.getFixParameters()) {
			q.setParameter(fixParam, formatters.get(formatterIndex).formatParameter(qr.getFixParameterValue(fixParam)));
			formatterIndex++;
		}
		if (args != null) {
			List<String> namedParameters = info.getNamedParemeters();
			
			Integer number = null;
			Integer size = info.getPageSize();
			
			for (int index = 0; index < args.length; index++) {
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
					Map<String, Object> paramMap = ReflectionUtils.toParameterMap(args[index]);
					for (int i = 0; i < namedParameters.size(); i++) {
						String param = namedParameters.get(i);
						Object value = paramMap.get(param);
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
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> namedParameters = info.getNamedParemeters();
			if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] != null)
						params.put(namedParameters.get(i), args[i]);
					else
						params.put(namedParameters.get(i), null);
				}
			} else if (info.getQueryStyle() == QueryStyle.QUERY_OBJECT) {
				Map<String, Object> paramMap = ReflectionUtils.toParameterMap(args[0]);
				for (int i = 0; i < namedParameters.size(); i++) {
					String param = namedParameters.get(i);
					Object value = paramMap.get(param);
					if (value != null)
						params.put(param, value);
					else
						params.put(namedParameters.get(i), null);
				}
			}
			return qr.getQuery(params).toString();
		}
	}

}
