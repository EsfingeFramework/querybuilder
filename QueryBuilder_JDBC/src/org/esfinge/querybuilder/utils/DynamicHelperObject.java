package org.esfinge.querybuilder.utils;

import java.util.Map;

import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

public class DynamicHelperObject {

	private String propertyName;
	private String operator;
	private NullOption typeOfNullOption;
	private Object valueOfProperty;
	private boolean isAddField;

	public boolean isAddField() {
		return isAddField;
	}

	public void setAddField(boolean isAddField) {
		this.isAddField = isAddField;
	}

	public DynamicHelperObject(String propertyName, String operator,
			NullOption typeOfNullOption, Object valueOfProperty,
			boolean isAddField) {

		setPropertyName(propertyName);
		setOperator(operator);
		setTypeOfNullOption(typeOfNullOption);
		setValueOfProperty(valueOfProperty);
		setAddField(isAddField);

	}

	public DynamicHelperObject() {

	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public NullOption getTypeOfNullOption() {
		return typeOfNullOption;
	}

	public void setTypeOfNullOption(NullOption typeOfNullOption) {
		this.typeOfNullOption = typeOfNullOption;
	}

	public Object getValueOfProperty() {
		return valueOfProperty;
	}

	public void setValueOfProperty(Object valueOfProperty) {
		this.valueOfProperty = valueOfProperty;
	}

	private String encapsulateValue(Object obj) {

		if (obj == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		String tipo = obj.getClass().getSimpleName().toUpperCase();

		if (tipo.equals("STRING")) {
			builder.append("'" + obj.toString().trim() + "'");
		} else {
			builder.append(obj);
		}
		return builder.toString();
	}

	public void updateValues(Map<String, Object> params) {

		for (String key : params.keySet()) {

			if (getPropertyName().equalsIgnoreCase(key)) {
				setValueOfProperty(encapsulateValue(params.get(key)));
			}

		}

		if (this.getTypeOfNullOption() != NullOption.NONE) {

			if (this.getTypeOfNullOption() == NullOption.COMPARE_TO_NULL) {

				this.setAddField(true);

			} else if (this.getTypeOfNullOption() == NullOption.IGNORE_WHEN_NULL) {

				if (this.getValueOfProperty() == null) {
					this.setAddField(false);
				} else {
					this.setAddField(true);
				}

			} else {
				this.setAddField(true);
			}

		}

	}

}
