package org.esfinge.querybuilder.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.esfinge.querybuilder.utils.DataBaseChannel;
import org.esfinge.querybuilder.utils.EntityParser;
import org.esfinge.querybuilder.utils.Line;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryStyle;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JDBCQueryExecutor implements QueryExecutor {

	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		DatabaseConnectionProvider dcp = ServiceLocator
				.getServiceImplementation(DatabaseConnectionProvider.class);
		Connection c = dcp.getConnection();

		// Ajustando a query com o formatador de parametros
		if (args != null) {

			for (int i = 0; i < args.length; i++) {

				if (args[i] != null) {

					args[i] = info.getCondition().getParameterFormatters()
							.get(i).formatParameter(args[i]);
				}

			}

		}

		JDBCQueryVisitor visitor = new JDBCQueryVisitor();
		if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {

			visitor.setValuesOfQuery(args);
		} else {

			Map<String, Object> paramMap = ReflectionUtils
					.toParameterMap(args[0]);
			List<String> namedParameters = info.getNamedParemeters();
			Object[] params = new Object[paramMap.size()];
			for (int i = 0; i < namedParameters.size(); i++) {
				String param = namedParameters.get(i);
				Object value = paramMap.get(param);
				if (value != null) {
					params[i] = value;
				}
			}

			visitor.setValuesOfQuery(params);
		}

		info.visit(visitor);

		String query = visitor.getJDBCQuery();

		DataBaseChannel channel = new DataBaseChannel(c);
		ArrayList<Line> mapResult = null;
		try {
			mapResult = channel.executeQuery(query);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		EntityParser parser = new EntityParser(info.getEntityType());
		ArrayList<Object> Result = null;

		try {
			Result = parser.parseEntity(mapResult);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception err) {
			err.printStackTrace();
		}

		if (Result != null) {
			if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
				return Result.get(0);
			}
		}

		return Result;
	}

}
