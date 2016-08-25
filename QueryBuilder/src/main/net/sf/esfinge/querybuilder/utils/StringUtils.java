package net.sf.esfinge.querybuilder.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.querybuilder.methodparser.IndexCounter;

public class StringUtils {

	public static String firstCharWithUppercase(String str) {
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}

	public static List<String> splitCamelCaseName(String name){
		List<String> names = new ArrayList<String>();
		String currentName = "";
		for(int i=0;i<name.length();i++){
			if(Character.isLowerCase(name.charAt(i))){
				currentName += name.charAt(i);
			}else if(!Character.isDigit(name.charAt(i))){
				if(Character.isUpperCase(name.charAt(i)) && 
						(currentName.length()==0 || Character.isUpperCase(name.charAt(i-1)))){
					if(currentName.length() == 1 && Character.isUpperCase(name.charAt(i))){
						currentName = currentName.toUpperCase();
					}
					currentName += name.charAt(i);
				}
				else{
					names.add(currentName);
					currentName = Character.toLowerCase(name.charAt(i))+"";
				}
			}
		}
		names.add(currentName);
		return names;
	}
	
	

	public static String removeSpacesToCamelCase(String name){
		name = name.trim();
		while(name.contains(" ")){
			int index = name.indexOf(" ");
			name = name.substring(0, index)+ name.substring(index+1, index+2).toUpperCase() + name.substring(index+2);
		}
		return name;
	}

	public static boolean matchString(String str, List<String> words, int index) {
		String[] strs = str.split(" ");
		if(strs.length > words.size() - index)
			return false;
		int counter = 0;
		for(String word : strs){
			if(!word.equals(words.get(index+counter)))
				return false;
			counter++;
		}
		return true;
	}

	public static String toCamelCase(String str) {
		while(str.indexOf(".") > 0){
			int index = str.indexOf(".");
			str = str.substring(0,index)+str.substring(index+1,index+2).toUpperCase()+str.substring(index+2);
		}
		return str;
	}


}
