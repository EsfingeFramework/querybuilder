package net.sf.esfinge.querybuilder.finder;

import java.util.HashMap;

public class Configurations {
	private HashMap<String, String> configurations;

	public Configurations() {
		configurations = new HashMap<String, String>();
	}

	public void addEntry(String key, String value) {
		try {
			configurations.put(key, value);
		} catch (Exception err) {
		}
	}

	public String findValueByKey(String key) {
		if (configurations.containsKey(key)) {
			return configurations.get(key);
		}
		return "";
	}

}