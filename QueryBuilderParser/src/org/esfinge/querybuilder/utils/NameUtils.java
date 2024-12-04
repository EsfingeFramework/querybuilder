package org.esfinge.querybuilder.utils;

import org.esfinge.querybuilder.methodparser.ComparisonType;

public class NameUtils {
	
    public static String acessorToProperty(String acessorName){
		int initLetter = 3;
		if(acessorName.startsWith("is")){
			initLetter = 2;
		}
		if(Character.isLowerCase(acessorName.charAt(initLetter+1)))
			return acessorName.substring(initLetter,initLetter+1).toLowerCase()+acessorName.substring(initLetter+1);
		else
			return acessorName.substring(initLetter);
    }

	public static String propertyToGetter(String propertieName, boolean isBoolean) {
		if(isBoolean)
			return "is"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
		return "get"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
	}
	
	public static String propertyToGetter(String propertieName) {
		return propertyToGetter(propertieName, false);
	}

	public static String propertyToSetter(String propertieName) {
		return "set"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
	}
	
	public static boolean endsWithComparisonOperator(String s){
		for(ComparisonType ct : ComparisonType.values()){
			if(s.endsWith(ct.getOpName())){
				return true;
			}
		}
		return false;
	}


}