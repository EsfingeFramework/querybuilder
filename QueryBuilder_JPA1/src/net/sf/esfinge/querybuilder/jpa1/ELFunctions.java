package net.sf.esfinge.querybuilder.jpa1;

import java.util.Map;

public class ELFunctions {

	public static boolean onlyNullValues(Map<String, Object> map) {
		for(String key : map.keySet()){
			if(map.get(key) != null)
				return false;
		}
		return true;
	}
	
	

}
