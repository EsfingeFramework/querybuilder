package net.sf.esfinge.querybuilder.jpa1;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import net.sf.esfinge.querybuilder.utils.StringUtils;

public class ConditionDescription {
	
	private String propertyName;
	private String paramName;
	private ComparisonType compType;
	private Object fixedValue;
	private NullOption nullOption = NullOption.NONE;
	private String nextConector = "";
	
	public ConditionDescription(String propertyName, ComparisonType compType) {
		super();
		this.propertyName = propertyName;
		this.paramName = StringUtils.toCamelCase(propertyName)+compType.getOpName();
		this.compType = compType;
	}
	
	public NullOption getNullOption() {
		return nullOption;
	}
	public void setNullOption(NullOption nullOption) {
		this.nullOption = nullOption;
	}
	public String getNextConector() {
		return nextConector;
	}
	public void setNextConector(String nextConector) {
		this.nextConector = nextConector;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public String getParamName() {
		return paramName;
	}
	public ComparisonType getCompType() {
		return compType;
	}
	public String getConditionString(){
		StringBuilder sb = new StringBuilder();
		if(nullOption != NullOption.NONE){
			sb.append("#{("+paramName+" != null)? '");
		}
		sb.append(" o."+propertyName+" "+compType.getOperator()+" :"+paramName);
		if(nullOption == NullOption.COMPARE_TO_NULL){
			sb.append("' : ' o."+propertyName+" IS NULL'}");
		}else if(nullOption == NullOption.IGNORE_WHEN_NULL){
			sb.append("' : ''}");
		}
		return sb.toString();
	}

	public Object getFixedValue() {
		return fixedValue;
	}
	public void setFixedValue(Object fixedValue) {
		this.fixedValue = fixedValue;
	}
}
