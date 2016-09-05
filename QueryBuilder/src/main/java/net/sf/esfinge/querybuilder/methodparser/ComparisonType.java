package net.sf.esfinge.querybuilder.methodparser;

import java.util.List;

public enum ComparisonType {
	EQUALS("=","Equals"), 
	GREATER_OR_EQUALS(">=","GreaterOrEquals"),
	LESSER_OR_EQUALS("<=","LesserOrEquals"),
	GREATER(">","Greater"),
	LESSER("<","Lesser"),
	NOT_EQUALS("<>","NotEquals"),
	CONTAINS("LIKE","Contains"),
	STARTS("LIKE","Starts"),
	ENDS("LIKE","Ends");
	
	private String operator;
	private String opName;
	
	private ComparisonType(String operator, String name){
		this.operator = operator;
		this.opName = name;
	}
	
	public String getOpName() {
		return opName;
	}

	private ComparisonType(){
	}

	public String getOperator() {
		return operator;
	}
	
	public boolean hasFixOperator(){
		return operator != null;
	}
	
	public int wordNumber() {
		if(this == EQUALS)
			return 0;
		return name().split("_").length;
	}

	public static ComparisonType getComparisonType(List<String> comparisonName, int index) {
		for(ComparisonType cp : values()){
			String[] values = cp.name().split("_");
			boolean flag = true;
			for(int i = 0; i<values.length; i++){
				if(comparisonName.size() <= index+i || !values[i].toLowerCase().equals(comparisonName.get(index+i))){
					flag = false;
				}
			}
			if(flag)
				return cp;
		}
		return EQUALS;
	}

	public static boolean isOperator(String firstWord) {
		for(ComparisonType cp : values()){
			String[] values = cp.name().split("_");
			if(values[0].toLowerCase().equals(firstWord))
				return true;
		}
		return false;
	}

	public static ComparisonType getComparisonType(String property) {
		for(ComparisonType comparisonType : values()){
			String comparison = comparisonType.name().replace("_", "");

			if (property.toUpperCase().contains(comparison)) {
				return comparisonType;
			}
		}
		
		return EQUALS;
	}

}
