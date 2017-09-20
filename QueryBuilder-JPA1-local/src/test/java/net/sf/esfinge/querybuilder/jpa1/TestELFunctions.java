package net.sf.esfinge.querybuilder.jpa1;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.querybuilder.jpa1.ELFunctions;

import org.junit.Test;

public class TestELFunctions {
	
	@Test
	public void mapWithNoValuesNull(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value1", "1");
		map.put("value2", 2);
		map.put("value3", 2.32);
		
		assertFalse(ELFunctions.onlyNullValues(map));
	}
	
	@Test
	public void mapWithOneValueNull(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value1", "1");
		map.put("value2", null);
		map.put("value3", 2.32);
		
		assertFalse(ELFunctions.onlyNullValues(map));
	}
	
	@Test
	public void mapWithAllValuesNull(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value1", null);
		map.put("value2", null);
		map.put("value3", null);
		
		assertTrue(ELFunctions.onlyNullValues(map));
	}

}
