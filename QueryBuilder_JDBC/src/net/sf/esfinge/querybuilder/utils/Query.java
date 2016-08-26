package net.sf.esfinge.querybuilder.utils;

import net.sf.esfinge.querybuilder.jdbc.CommandType;
import net.sf.esfinge.querybuilder.utils.reflection.ReflectionOperations;

public class Query {

	private String command;
	private CommandType commandType;
	private Object obj;
	private Object idValue;

	public Object getIdValue() {
		return idValue;
	}

	public void setIdValue(Object idValue) {
		this.idValue = idValue;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {

		this.obj = obj;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public String buildCommand() throws Exception {

		Line line = DataExtractor.extract(this.obj);

		switch (commandType) {
		case SELECT_ALL:
			command = createSelect(line);
			break;

		case SELECT_SINGLE:
			command = createSelect(line);
			break;

		case SELECT_EXISTS:
			command = createSelect(line);
			break;

		case SELECT_BY_EXAMPLE:
			command = createSelectByExample(line);
			break;

		case INSERT:
			command = createInsert(line);
			break;

		case UPDATE:
			command = createUpdate(line);
			break;

		case DELETE:
			command = createDelete(line);
			break;

		default:
			command = "";
			break;
		}

		return command;

	}

	private String createUpdate(Line line) {

		StringBuilder builder = new StringBuilder();
		SQLUtils sqlUtils = new SQLUtils(obj.getClass().getSimpleName());
		ReflectionOperations rfp = new ReflectionOperations();

		builder.append("update ");
		builder.append(sqlUtils.getMainEntity() + " ");
		builder.append("set ");

		boolean firstValue = true;
		for (String column : sqlUtils.getListOfColumnsToUpdateComand()) {

			if (!firstValue) {

				builder.append(", ");
			}

			Object value = null;

			if (sqlUtils.isJoinColumn(column)) {
				value = rfp.findAttributeInClass(getObj(), column);
			} else {
				value = line.getValueByFieldName(column);
			}

			if (value == null) {
				builder.append(column + " = null");
			} else {
				builder.append(column + " = "
						+ encapsulateValue(value).toString());
			}

			firstValue = false;

		}

		builder.append(" where ");
		builder.append(sqlUtils.getPrimaryKeyOfMainEntity()
				+ " = "
				+ encapsulateValue(
						line.getValueByFieldName(sqlUtils
								.getPrimaryKeyOfMainEntity())).toString());

		return builder.toString();

	}

	private String createSelect(Line line) {

		StringBuilder builder = new StringBuilder();
		SQLUtils sqlUtils = new SQLUtils(obj.getClass().getSimpleName());

		builder.append("select ");
		builder.append(sqlUtils.getFieldsEntities());
		builder.append(" from ");
		builder.append(sqlUtils.getChildEntities());

		if ((commandType == CommandType.SELECT_SINGLE)) {
			builder.append(" where "
					+ sqlUtils.getMainEntity()
					+ "."
					+ sqlUtils.getPrimaryKeyOfMainEntity()
					+ " = "
					+ (getIdValue() == null ? line.getValueByFieldName(
							line.findPrimaryKey()).toString() : getIdValue()));

			if (sqlUtils.haveJoinColumn()) {

				builder.append(" and "
						+ sqlUtils.getJoinExpressions().toLowerCase());

			}

		} else if (commandType == CommandType.SELECT_EXISTS) {

			builder.append(" where "
					+ sqlUtils.getMainEntity()
					+ "."
					+ sqlUtils.getPrimaryKeyOfMainEntity()
					+ " = "
					+ line.getValueByFieldName(sqlUtils
							.getPrimaryKeyOfMainEntity()));

			if (sqlUtils.haveJoinColumn()) {
				builder.append(" and "
						+ sqlUtils.getJoinExpressions().toLowerCase());
			}

		} else {

			if (!sqlUtils.haveJoinColumn()) {
				builder.append(" where 1=1");
			} else {

				builder.append(" where "
						+ sqlUtils.getJoinExpressions().toLowerCase());
			}
		}

		return builder.toString();
	}

	private String encapsulateValue(Object obj) {

		StringBuilder builder = new StringBuilder();

		if (obj != null) {
			String tipo = obj.getClass().getSimpleName().toUpperCase();

			if (tipo.equals("STRING")) {

				builder.append("'");
				builder.append(obj.toString().trim());
				builder.append("'");

			} else {

				builder.append(obj);
			}
		}
		return builder.toString();

	}

	private String createInsert(Line line) {

		StringBuilder builder = new StringBuilder();
		StringBuilder builderValues = new StringBuilder();
		ReflectionOperations rfp = new ReflectionOperations();
		SQLUtils sqlUtils = new SQLUtils(obj.getClass().getSimpleName());

		builder.append("insert into ");
		builder.append(sqlUtils.getMainEntity() + " ");
		builder.append(sqlUtils.getColumnsToInsertComand());
		builder.append(" values");
		builder.append(" (");

		boolean firstValue = true;
		for (String column : sqlUtils.getListOfColumnsToInsertComand()) {

			if (!firstValue) {
				builderValues.append(",");
			}

			if (column.contains(".")) {

				column.indexOf(".");
				column = (column.substring((column.indexOf(".") + 1)));

			}

			Object value = null;

			if (sqlUtils.isJoinColumn(column)) {

				value = rfp.findAttributeInClass(obj, column);

			} else {

				value = line.getValueByFieldName(column);

			}

			if (value == null) {
				builderValues.append("null");
			} else {
				builderValues.append(encapsulateValue(value).toString());
			}

			firstValue = false;
		}

		builder.append(builderValues.toString());
		builder.append(")");

		return builder.toString();

	}

	private String createDelete(Line line) {

		String entityName = obj.getClass().getSimpleName();
		String IdName = line.findPrimaryKey();
		String IdValue = (idValue != null ? idValue : line
				.getValueByFieldName(IdName)).toString();
		String query = String.format("delete from %s where %s = %s",
				entityName, IdName, IdValue).toLowerCase();
		return query;
	}

	private String createSelectByExample(Line line) {

		StringBuilder builder = new StringBuilder();
		SQLUtils sqlUtils = new SQLUtils(obj.getClass().getSimpleName());

		builder.append("select ");
		builder.append(sqlUtils.getFieldsEntities());
		builder.append(" from ");
		builder.append(sqlUtils.getChildEntities());

		boolean firstValue = true;

		for (String column : sqlUtils.getListOfFieldsEntities()) {

			if (line.getValueByFieldName(column.substring((column.indexOf(".") + 1))) == null) {
				continue;
			}

			if (firstValue) {
				firstValue = false;
				builder.append(" where ");
				builder.append(column
						+ " = "
						+ encapsulateValue(line.getValueByFieldName(column
								.substring((column.indexOf(".") + 1)))));
			} else {
				builder.append(" and ");
				builder.append(column
						+ " = "
						+ encapsulateValue(line.getValueByFieldName(column
								.substring((column.indexOf(".") + 1)))));
			}

		}

		if (firstValue) {
			builder.append(" where 1=1");
		}

		if (sqlUtils.haveJoinColumn()) {
			builder.append(" and "
					+ sqlUtils.getJoinExpressions().toLowerCase());
		}

		return builder.toString();

	}

}
