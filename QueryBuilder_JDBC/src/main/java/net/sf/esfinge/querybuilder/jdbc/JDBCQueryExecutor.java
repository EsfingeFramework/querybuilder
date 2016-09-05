package net.sf.esfinge.querybuilder.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryStyle;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import net.sf.esfinge.querybuilder.utils.DataBaseChannel;
import net.sf.esfinge.querybuilder.utils.EntityParser;
import net.sf.esfinge.querybuilder.utils.Line;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JDBCQueryExecutor implements QueryExecutor {

	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		DatabaseConnectionProvider dcp = ServiceLocator.getServiceImplementation(DatabaseConnectionProvider.class);
		Connection c = dcp.getConnection();

		JDBCQueryVisitor visitor = new JDBCQueryVisitor();
		if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] != null) {
						String param = info.getNamedParemeters().get(i);
						
						if (args[i] != null) {
							ComparisonType cp = ComparisonType.getComparisonType(param);
							FormaterFactory factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
							args[i] = factory.getFormater(cp).formatParameter(args[i]);
						}
					}
				}
			}
			
			visitor.setValuesOfQuery(args);
		} else {
			Map<String, Object> map = ReflectionUtils.toParameterMap(args[0]);
			List<String> parameters = info.getNamedParemeters();
			Object[] params = new Object[map.size()];
			
			for (int i = 0; i < parameters.size(); i++) {
				String param = parameters.get(i);
				Object value = map.get(param);
				
				if (value != null) {
					ComparisonType cp = ComparisonType.getComparisonType(param);
					FormaterFactory factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
					params[i] = factory.getFormater(cp).formatParameter(value);
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
