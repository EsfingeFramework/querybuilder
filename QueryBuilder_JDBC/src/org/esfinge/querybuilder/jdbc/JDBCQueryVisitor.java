package org.esfinge.querybuilder.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.jdbc.QueryElement;
import org.esfinge.querybuilder.utils.DynamicHelperObject;
import org.esfinge.querybuilder.utils.QueryFormatter;
import org.esfinge.querybuilder.utils.SQLUtils;

import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

public class JDBCQueryVisitor implements QueryVisitor {

	private StringBuilder stQuery = new StringBuilder();
	private StringBuilder stOrderByQuery = new StringBuilder();
	private Object[] valuesOfQuery;
	private int orderCondition = 0;
	private QueryElement lastCalled = QueryElement.NONE;
	private Map<String, Object> fixParameters = new HashMap<String, Object>();
	private List<DynamicHelperObject> listOfDynamicsObjetcs = new ArrayList<DynamicHelperObject>();
	private String entity;
	private SQLUtils sqlUtils = null;
	private int contNullValues = 0;
	private int contIgnored = -1;

	public List<DynamicHelperObject> getListOfDynamicsObjetcs() {
		return listOfDynamicsObjetcs;
	}

	public SQLUtils getSqlUtils() {
		return sqlUtils;
	}

	public Object[] getValuesOfQuery() {
		return valuesOfQuery;
	}

	public void setValuesOfQuery(Object[] valuesOfQuery) {
		this.valuesOfQuery = valuesOfQuery;
	}

	public String getStQuery() {
		return stQuery.toString();
	}

	public StringBuilder getOriginalStQuery() {
		return stQuery;
	}

	public String getEntity() {
		return entity.toLowerCase().trim();
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	@Override
	public void visitEntity(String entity) {

		if (lastCalled != QueryElement.NONE)
			throw new InvalidQuerySequenceException(
					"Entity(JDBC) should be called only in the begining.");
		this.entity = entity;

		sqlUtils = new SQLUtils(entity);

		StringBuilder stFrom = new StringBuilder();

		stQuery.append("select");
		stQuery.append(" ");
		stQuery.append(sqlUtils.getFieldsEntities());

		stFrom.append(" ");
		stFrom.append("from");
		stFrom.append(" ");

		stFrom.append(sqlUtils.getChildEntities());

		stQuery.append(stFrom.toString());
		lastCalled = QueryElement.ENTITY;

	}

	@Override
	public void visitConector(String conector) {

		if (lastCalled != QueryElement.CONDITION)
			throw new InvalidQuerySequenceException(
					"Conector(JDBC) called in wrong sequence.");
		stQuery.append(" ");
		stQuery.append(conector.toLowerCase());
		lastCalled = QueryElement.CONECTOR;

	}

	@Override
	public void visitCondition(String propertyName, ComparisonType op) {

		DynamicHelperObject dynamic = new DynamicHelperObject(propertyName,
				op.getOperator(), null, null, false);
		listOfDynamicsObjetcs.add(dynamic);

		String paramName = propertyName
				.substring(propertyName.lastIndexOf(".") + 1);
		addCondition(propertyName, op, paramName, false, false, null);
	}

	public String getJDBCQuery() {

		if (lastCalled == QueryElement.CONECTOR)
			throw new InvalidQuerySequenceException(
					"Conector(JDBC) should not be the last element.");

		return replaceNullOptions(getStQuery());
	}

	public void visitOrderBy(String property, OrderingDirection order) {

		if (lastCalled == QueryElement.CONDITION
				|| lastCalled == QueryElement.ENTITY) {
			if (!stOrderByQuery.toString().toLowerCase().contains("order by")) {
				stOrderByQuery.append(" order by ");
			} else {
				stOrderByQuery.append(", ");
			}
			stOrderByQuery.append(property);
			stOrderByQuery.append(((order == OrderingDirection.ASC) ? " ASC "
					: " DESC"));

		} else {
			throw new InvalidQuerySequenceException(
					"Condition(JDBC) called in wrong sequence.");
		}
		lastCalled = QueryElement.CONDITION;

	}

	private void addCondition(String propertyName, ComparisonType op,
			String paramName, boolean isDomainTerm, boolean isNullOption,
			NullOption nullOption) {

		boolean ignoreNullOption = false;
		boolean compareToNull = false;
		orderCondition = (orderCondition + 1);
		contIgnored++;

		if (isNullOption) {

			if (nullOption == NullOption.IGNORE_WHEN_NULL) {
				if (valuesOfQuery != null && valuesOfQuery[contIgnored] == null) {
					ignoreNullOption = true;
				} else {
					ignoreNullOption = false;
				}
			} else if (nullOption == NullOption.COMPARE_TO_NULL) {
				ignoreNullOption = false;

				if (valuesOfQuery != null && valuesOfQuery[contIgnored] == null) {
					compareToNull = true;
				} else {
					compareToNull = false;
				}
			}
		}
		if (lastCalled == QueryElement.ENTITY) {

			stQuery.append(" ");
			stQuery.append("where");
			stQuery.append(" ");

			if (isNullOption) {
				if (!ignoreNullOption) {

					if (!propertyName.contains(".")) {

						stQuery.append(getEntity());
						stQuery.append(".");
					}

					if (compareToNull) {
						stQuery.append(propertyName);
						stQuery.append(" ");
						stQuery.append("is null");

					} else {

						stQuery.append(propertyName);
						stQuery.append(" ");
						stQuery.append(op.getOperator());
					}
				}

			} else {

				if (!propertyName.contains(".")) {

					stQuery.append(getEntity());
					stQuery.append(".");
				}

				stQuery.append(propertyName);
				stQuery.append(" ");
				stQuery.append(op.getOperator());
			}

			if (isDomainTerm) {
				stQuery.append(" " + paramName);
			} else {
				if (isNullOption) {

					if (!ignoreNullOption && !compareToNull) {

						contNullValues++;
						stQuery.append(" " + contNullValues + "?");

					}

				} else {
					stQuery.append(" " + orderCondition + "?");
				}

			}

		} else if (lastCalled == QueryElement.CONECTOR) {

			if (isNullOption) {
				if (!ignoreNullOption) {
					stQuery.append(" ");

					if (!propertyName.contains(".")) {

						stQuery.append(getEntity());
						stQuery.append(".");
					}

					if (compareToNull) {
						stQuery.append(propertyName);
						stQuery.append(" ");
						stQuery.append("is null");

					} else {

						stQuery.append(propertyName);
						stQuery.append(" ");
						stQuery.append(op.getOperator());
					}

				}

			} else {

				stQuery.append(" ");

				if (!propertyName.contains(".")) {

					stQuery.append(getEntity());
					stQuery.append(".");
				}

				stQuery.append(propertyName);
				stQuery.append(" ");
				stQuery.append(op.getOperator());
			}

			if (isDomainTerm) {
				stQuery.append(" " + paramName);
			} else {

				if (isNullOption) {

					if (!ignoreNullOption && !compareToNull) {
						contNullValues++;
						stQuery.append(" " + contNullValues + "?");
					}

				} else {
					stQuery.append(" " + orderCondition + "?");
				}

			}

		} else {
			throw new InvalidQuerySequenceException(
					"Condition called in wrong sequence");
		}
		lastCalled = QueryElement.CONDITION;

	}

	public void visitCondition(String propertyName, ComparisonType operator,
			Object fixedValue) {
		String paramName = propertyName
				.substring(propertyName.lastIndexOf(".") + 1) + operator.name();

		addCondition(propertyName, operator, paramName, true, false, null);

		fixParameters.put(paramName, fixedValue);
	}

	public Object getFixParameterValue(String param) {
		return fixParameters.get(param);
	}

	public Set<String> getFixParameters() {
		return fixParameters.keySet();
	}

	public void visitCondition(String propertyName, ComparisonType operator,
			NullOption nullOption) {

		DynamicHelperObject dynamic = new DynamicHelperObject(propertyName,
				operator.getOperator(), nullOption, null, false);
		listOfDynamicsObjetcs.add(dynamic);

		String paramName = propertyName
				.substring(propertyName.lastIndexOf(".") + 1);

		addCondition(propertyName, operator, paramName, false, true, nullOption);

	}

	public void visitEnd() {

		if (lastCalled == QueryElement.CONECTOR)
			throw new InvalidQuerySequenceException(
					"Conector should not be the last element");

		if (stQuery.toString().trim()
				.substring((stQuery.length() - 3), stQuery.length())
				.equalsIgnoreCase("and")) {

			stQuery.delete((stQuery.length() - 3), stQuery.length());

		}

		if (sqlUtils.haveJoinColumn()) {

			if (stQuery.toString().contains("where")) {
				stQuery.append(" and ");
			} else {
				stQuery.append(" where ");
			}

			stQuery.append(sqlUtils.getJoinExpressions());

		}

		stQuery.append(stOrderByQuery.toString());
		buildFinalQueryWithValues();
		contNullValues = 0;
		orderCondition = 0;
		contIgnored = -1;

	}

	public boolean isDynamic() {

		for (DynamicHelperObject dynamic : listOfDynamicsObjetcs) {

			if (dynamic.getTypeOfNullOption() != NullOption.NONE) {
				return true;
			}
		}
		return false;
	}

	public String getQuery() {

		return getStQuery().toLowerCase();
	}

	public String getQuery(Map<String, Object> params) {

		throw new UnsupportedOperationException();
	}

	private String replaceNullOptions(String query) {

		query = query.replace("and and", "and");
		query = query.replace("where  and", "where");
		query = query.replace("where and", "where");
		return query;
	}

	private void buildFinalQueryWithValues() {

		QueryFormatter queryFormatter = new QueryFormatter();

		String finalQuery = queryFormatter.putValuesOnQuery(getJDBCQuery(),
				getValuesOfQuery(), fixParameters);

		stQuery.delete(0, stQuery.length());
		stQuery.append(finalQuery);

	}

	@Override
	public QueryRepresentation getQueryRepresentation() {

		JDBCQueryRepresentation jdbcQR = new JDBCQueryRepresentation(
				getQuery(), isDynamic(), fixParameters, this);
		return jdbcQR;
	}

}
