package org.esfinge.querybuilder.methodparser.conversor;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConversorTests {
	
	@Test
	public void toIntTest(){
		Object value = ConversorFactory.get(int.class).convert("23");
		assertEquals(value, new Integer(23));
	}
	
	@Test
	public void toShortTest(){
		Object value = ConversorFactory.get(short.class).convert("23");
		assertEquals(value, new Short((short)23));
	}
	
	@Test
	public void toByteTest(){
		Object value = ConversorFactory.get(byte.class).convert("23");
		assertEquals(value, new Byte((byte)23));
	}
	
	@Test
	public void toLongTest(){
		Object value = ConversorFactory.get(long.class).convert("23");
		assertEquals(value, new Long(23));
	}
	
	@Test
	public void toBooleanTest(){
		Object value = ConversorFactory.get(boolean.class).convert("true");
		assertEquals(value, Boolean.TRUE);
	}
	
	@Test
	public void toFloatTest(){
		Object value = ConversorFactory.get(float.class).convert("23");
		assertEquals(value, new Float(23));
	}
	
	@Test
	public void toDoubleTest(){
		Object value = ConversorFactory.get(double.class).convert("23");
		assertEquals(value, new Double(23));
	}
	
	

}
