package org.esfinge.querybuilder.jdbc;

import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.utils.DynamicHelperObject;

public class JDBCQueryRepresentation implements QueryRepresentation {

	private String jdbcQuery;
	private boolean dynamic;
	private Map<String, Object> fixParameters;
	private JDBCQueryVisitor jdbcVisitor;

	public JDBCQueryRepresentation(String jdbcQuery, boolean dynamic,
			Map<String, Object> fixParameters,
			JDBCQueryVisitor jdbcVisitor) {
		this.jdbcQuery = jdbcQuery;
		this.dynamic = dynamic;
		this.fixParameters = fixParameters;
		this.jdbcVisitor = jdbcVisitor;
	}

	public boolean isDynamic() {

		return dynamic;
	}

	public Object getQuery() {

		return jdbcQuery;
	}

	public Object getQuery(Map<String, Object> params) {
		boolean isFirst = true;

		StringBuilder stQuery = new StringBuilder();
		stQuery = jdbcVisitor.getOriginalStQuery();

		stQuery.delete(0, stQuery.length());
		stQuery.append("select ");
		stQuery.append(jdbcVisitor.getSqlUtils().getFieldsEntities());
		stQuery.append(" from ");
		stQuery.append(jdbcVisitor.getSqlUtils().getChildEntities());

		for (DynamicHelperObject dynamic : jdbcVisitor
				.getListOfDynamicsObjetcs()) {

			dynamic.updateValues(params);

			if (dynamic.isAddField()) {

				if (isFirst) {
					stQuery.append(" where ");
					isFirst = false;

				} else {
					stQuery.append(" and ");
				}

				stQuery.append(dynamic.getPropertyName() + " ");
				stQuery.append(dynamic.getOperator() + " ");
				stQuery.append(dynamic.getValueOfProperty());

			}

		}

		if (jdbcVisitor.getSqlUtils().haveJoinColumn()) {

			if (stQuery.toString().contains("where")) {
				stQuery.append(" and ");
			} else {
				stQuery.append(" where ");
			}

			stQuery.append(jdbcVisitor.getSqlUtils().getJoinExpressions());

		}

		return jdbcVisitor.getStQuery();
	}

	public Set<String> getFixParameters() {

		return fixParameters.keySet();
	}

	public Object getFixParameterValue(String param) {

		return fixParameters.get(param);
	}

}
