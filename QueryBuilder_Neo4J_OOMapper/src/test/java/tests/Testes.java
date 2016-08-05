package tests;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.SortField;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.neo4j.oomapper.Condition;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.neo4j.oomapper.Query;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.NoNodeEntityException;
import org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions.UnindexedPropertyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.IndexedPropertyPerson;
import entities.IterablePropertiesPerson;
import entities.NotAnnotadedPerson;
import entities.RelatedPerson;
import entities.RelatedSetPerson;
import entities.SimplePerson;


public class Testes {

	Neo4J neo;
	
	@Before
	public void initializeDB(){
		neo = new Neo4J();
//		neo = new Neo4J("neo4j-236");
	}
	
	@After
	public void clearDB(){
		neo.clearDB();
		neo.shutdown();
	}
	
	@Test(expected = NoNodeEntityException.class)
	public void nodeEntityAnnotationNotPresentTest(){
		neo.map(NotAnnotadedPerson.class);
	}
	
	@Test
	public void mappingTest(){
		neo.map(SimplePerson.class);
		assertEquals(SimplePerson.class, neo.getEntityClass("SimplePerson"));
		assertEquals(SimplePerson.class, neo.getEntityClass("simpleperson"));
	}
	
	@Test
	public void simpleSaveTest(){
		neo.map(SimplePerson.class);
		SimplePerson p = new SimplePerson();
		p.setNome("Fulano");
		p.setSobrenome("da Silva");
		p.setIdade(15);
		neo.save(p);
		
		Query<SimplePerson> q = neo.query(SimplePerson.class);
		q.setProperty("nome", "Fulano");
		
		List<SimplePerson> results = q.asList();
		for(SimplePerson per : results){
			assertEquals("Fulano", per.getNome());
			assertEquals(15, per.getIdade());
			assertEquals("da Silva", per.getSobrenome());
		}
	}
	
	@Test(expected = UnindexedPropertyException.class)
	public void unindexedPropertyQuery(){
		neo.map(SimplePerson.class);
		neo.query(SimplePerson.class).setProperty("sobrenome", "indiferente");
	}
	
	@Test
	public void relatedSaveTest(){
		neo.map(RelatedPerson.class);
		RelatedPerson p = new RelatedPerson();
		
		p.setNome("Tem amigo");
		
		neo.map(SimplePerson.class);
		SimplePerson sp = new SimplePerson();
		sp.setNome("Amigo");
		sp.setIdade(17);
		sp.setSobrenome("Sobrenome");
		
		p.setAmigo(sp);
		
		neo.save(p);
		
		Query<RelatedPerson> q = neo.query(RelatedPerson.class);
		q.setProperty("nome", "Tem amigo");
		RelatedPerson retrievedRelatedPerson = q.getSingle();
		
		assertEquals("Tem amigo", retrievedRelatedPerson.getNome());
		assertEquals("Amigo", retrievedRelatedPerson.getAmigo().getNome());
		assertEquals(17, retrievedRelatedPerson.getAmigo().getIdade());
		assertEquals("Sobrenome", retrievedRelatedPerson.getAmigo().getSobrenome());
	}
	
	@Test
	public void indexedSimpleProperties(){
		
		neo.map(IndexedPropertyPerson.class);
		
		IndexedPropertyPerson p = new IndexedPropertyPerson();
		int[] nums = {1, 2, 3, 7, 4, 9};
		p.setNumeros(nums);
		p.setNome("Fulano");
		String[] strings = {"oi", "isso �", "um teste"};
		p.setStrings(strings);
		p.setIdade(20);
		p.setSobrenome("De Tal");
		
		neo.save(p);
		Query<IndexedPropertyPerson> q = neo.query(IndexedPropertyPerson.class);
		q.setProperty("nome", "Fulano");
		IndexedPropertyPerson retrieved = q.getSingle();
		
		assertEquals("Fulano", retrieved.getNome());
		assertEquals("De Tal", retrieved.getSobrenome());
		assertEquals(20, retrieved.getIdade());
		for(int i = 0; i < nums.length; i++)
			assertEquals(nums[i], retrieved.getNumeros()[i]);
		
		for(int i = 0; i < strings.length; i++)
			assertEquals(strings[i], retrieved.getStrings()[i]);
		
	}
	
	@Test
	public void iterableProperties(){
		
		neo.map(IterablePropertiesPerson.class);
		
		IterablePropertiesPerson p = new IterablePropertiesPerson();
		p.setNome("Fulano");
		LinkedList<Integer> numeros = new LinkedList<Integer>();
		numeros.add(1);
		numeros.add(3);
		p.setNumeros(numeros);
		float[] floats = new float[] {0.5f, 1.4f, 3.2f};
		p.setFloats(floats);
		Integer[] integers = {1, 3, 5};
		p.setIntegers(integers);
		
		neo.save(p);
		
		Query<IterablePropertiesPerson> q = neo.query(IterablePropertiesPerson.class);
		q.setProperty("nome", "Fulano");
		IterablePropertiesPerson retrieved = q.getSingle();
		
		assertEquals("Fulano", retrieved.getNome());
		assertEquals(numeros, retrieved.getNumeros());
		for(int i = 0; i < floats.length; i++)
			assertEquals(floats[i], retrieved.getFloats()[i], 0.00001);
		for(int i = 0; i < integers.length; i++)
			assertEquals(integers[i], retrieved.getIntegers()[i]);
	}
	
	@Test
	public void relatedSet(){
		
		neo.map(RelatedSetPerson.class);
		
		RelatedSetPerson p = new RelatedSetPerson();
		p.setNome("Fulano");
		
		neo.map(SimplePerson.class);
		SimplePerson sp = new SimplePerson();
		sp.setNome("Amigo");
		sp.setIdade(17);
		sp.setSobrenome("Sobrenome");
		
		Set<SimplePerson> amigos = new HashSet<SimplePerson>();
		amigos.add(sp);
		p.setAmigos(amigos);
		
		neo.save(p);
		
		Query<RelatedSetPerson> q = neo.query(RelatedSetPerson.class);
		
		q.setProperty("nome", "Fulano");
		
		RelatedSetPerson retrieved = q.getSingle();
		SimplePerson retrievedFriend = retrieved.getAmigos().iterator().next();
		
		assertEquals("Fulano", retrieved.getNome());
		assertEquals("Amigo", retrievedFriend.getNome());
		assertEquals(17, retrievedFriend.getIdade());
		assertEquals("Sobrenome", retrievedFriend.getSobrenome());
		
	}
	
	@Test
	public void conditionTest(){

		neo.map(SimplePerson.class);
		SimplePerson fulano = new SimplePerson();
		fulano.setNome("Fulano");
		fulano.setIdade(17);
		fulano.setSobrenome("Sobrenome do Fulano");
		
		SimplePerson ciclano = new SimplePerson();
		ciclano.setNome("Ciclano");
		ciclano.setIdade(21);
		ciclano.setSobrenome("Sobrenome do Ciclano");
		
		Query<SimplePerson> q1 = neo.query(SimplePerson.class);
		
		q1.or(new Condition("nome", "Fulano"), new Condition("nome", "Ciclano"));
		List<SimplePerson> results = q1.asList();
		
		for(SimplePerson person : results){
			if(person.getNome().equals("Fulano")){
				assertEquals(17, person.getIdade());
				assertEquals("Sobrenome do Fulano", person.getSobrenome());
			}
			else if(person.getNome().equals("Ciclano")){
				assertEquals(21, person.getIdade());
				assertEquals("Sobrenome do Ciclano", person.getSobrenome());
			}
			else
				assertEquals(1, 2);
		}
		
	}
	
	@Test
	public void deleteTest(){
		
		neo.map(SimplePerson.class);
		SimplePerson p = new SimplePerson();
		p.setNome("Fulano");
		p.setSobrenome("da Silva");
		p.setIdade(15);
		neo.save(p);
		
		Query<SimplePerson> q = neo.query(SimplePerson.class);
		q.setProperty("nome", "Fulano");
		
		List<SimplePerson> results = q.asList();
		for(SimplePerson per : results){
			assertEquals("Fulano", per.getNome());
			assertEquals(15, per.getIdade());
			assertEquals("da Silva", per.getSobrenome());
		}
		
		neo.delete(SimplePerson.class, "Fulano");
		assertEquals(0, q.asList().size());
	}
	
	@Test
	public void numericRangeQuery(){
		
		neo.map(SimplePerson.class);
		SimplePerson p = new SimplePerson();
		p.setNome("Fulano");
		p.setSobrenome("da Silva");
		p.setIdade(15);
		neo.save(p);
		
		p = new SimplePerson();
		p.setNome("Ciclano");
		p.setSobrenome("da Silva");
		p.setIdade(13);
		neo.save(p);
		
		Query<SimplePerson> q = neo.query(SimplePerson.class);
		
		q.or(new Condition("idade", 14, ComparisonType.GREATER));
		
		assertEquals("Fulano", q.getSingle().getNome());
		
	}
	
	@Test
	public void sort(){
		SortField sf = new SortField("campo", SortField.INT);
		System.out.println(sf);
	}
}
