package net.sf.esfinge.querybuilder.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.sf.esfinge.querybuilder.utils.StringUtils;


public class StringUtilsTest {
	
	@Test
	public void simpleName(){
		List<String> names = StringUtils.splitCamelCaseName("nome");
		assertEquals(1,names.size());
		assertEquals("nome", names.get(0));
	}
	
	@Test
	public void compositeName(){
		List<String> names = StringUtils.splitCamelCaseName("nomeComposto");
		assertEquals(2,names.size());
		assertEquals("nome", names.get(0));
		assertEquals("composto", names.get(1));
	}
	
	@Test
	public void sigla(){
		List<String> names = StringUtils.splitCamelCaseName("CPF");
		assertEquals(1,names.size());
		assertEquals("CPF", names.get(0));
	}
	
	@Test
	public void siglaNoMeio(){
		List<String> names = StringUtils.splitCamelCaseName("numeroCPF");
		assertEquals(2,names.size());
		assertEquals("numero", names.get(0));
		assertEquals("CPF", names.get(1));
	}
	
	@Test
	public void numberInTheMiddle(){
		List<String> names = StringUtils.splitCamelCaseName("nome123Composto");
		assertEquals(2,names.size());
		assertEquals("nome", names.get(0));
		assertEquals("composto", names.get(1));
	}
	
	@Test
	public void joinString(){
		String result = StringUtils.removeSpacesToCamelCase("teste aqui novamente");
		assertEquals("testeAquiNovamente", result);
	}
	
	@Test
	public void matchSimpleString(){
		String[] words = {"string", "a", "b"};
		boolean result = StringUtils.matchString("string", Arrays.asList(words), 0);
		assertTrue("Should match", result);
	}

	@Test
	public void matchFailed(){
		String[] words = {"str", "a", "b"};
		boolean result = StringUtils.matchString("string", Arrays.asList(words), 0);
		assertFalse("Shouldn't match", result);
	}
	
	@Test
	public void matchCompositeString(){
		String[] words = {"c", "string", "here", "b"};
		boolean result = StringUtils.matchString("string here", Arrays.asList(words), 1);
		assertTrue("Should match", result);
	}
	
	@Test
	public void notMatchCompositeString(){
		String[] words = {"c", "string", "a", "b"};
		boolean result = StringUtils.matchString("string here", Arrays.asList(words), 1);
		assertFalse("Shouldn't match", result);
	}
	
	@Test
	public void toCamelCaseTwoWords(){
		String transform = "address.city";
		String result = StringUtils.toCamelCase(transform);
		assertEquals("addressCity", result);
	}
	
	@Test
	public void oneWord(){
		String transform = "address";
		String result = StringUtils.toCamelCase(transform);
		assertEquals("address", result);
	}
	
	@Test
	public void toCamelCaseManyWords(){
		String transform = "address.city.street.name";
		String result = StringUtils.toCamelCase(transform);
		assertEquals("addressCityStreetName", result);
	}
	

}
