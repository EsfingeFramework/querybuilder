package org.esfinge.querybuilder.jpa1.queryobjects;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.esfinge.querybuilder.jpa1.testresources.Person;
import org.esfinge.querybuilder.jpa1.testresources.QueryBuilderDatabaseTest;
import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;

public class QueryBuilderJPAQueryObject extends QueryBuilderDatabaseTest {
	
	private TestQueryObject tq;

	@Before
	public void setupDatabase() throws Exception {
		initializeDatabase("/initial_db_queryobject.xml");
		tq = QueryBuilder.create(TestQueryObject.class);
	}
	
	@Test
	public void simpleQueryObject(){
		SimpleQueryObject qo = new SimpleQueryObject();
		qo.setLastName("Silva");
		qo.setAge(20);
		qo.setAddressState("SP");
		Person p = tq.getPerson(qo);
		assertEquals(new Integer(1),p.getId());		
	}
	
	
	@Test
	public void queryObjectWithComparisonType(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(18);
		qo.setAgeLesser(40);
		qo.setName("o");
		qo.setLastName("i");
		List<Person> list = tq.getPerson(qo);
		Person p = list.get(0);
		assertEquals(1,list.size());
		assertEquals(new Integer(1),p.getId());		
	}
	
	@Test
	public void queryObjectWithDomainTerm(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(1);
		qo.setAgeLesser(100);
		qo.setName("a");
		qo.setLastName("e");
		List<Person> list = tq.getPersonPaulista(qo);
		Person p = list.get(0);
		assertEquals(1,list.size());
		assertEquals(new Integer(2),p.getId());		
	}
	
	@Test
	public void queryObjectWithNullComparison(){
		CompareNullQueryObject qo = new CompareNullQueryObject();
		qo.setName("M");
		List<Person> list = tq.getPerson(qo);
		assertEquals(1,list.size());
		Person p = list.get(0);
		assertEquals(new Integer(3),p.getId());		
	}
	
	@Test
	public void queryObjectIgnoreNull(){
		CompareNullQueryObject qo = new CompareNullQueryObject();
		qo.setLastName("B");
		List<Person> list = tq.getPerson(qo);
		assertEquals(1,list.size());
		Person p = list.get(0);
		assertEquals(new Integer(5),p.getId());		
	}
	
	@Test
	public void queryObjectWithOrderBy(){
		ComparisonTypeQueryObject qo = new ComparisonTypeQueryObject();
		qo.setAgeGreater(1);
		qo.setAgeLesser(100);
		qo.setName("a");
		qo.setLastName("e");
		
		List<Person> list = tq.getPersonOrderByNameAsc(qo);
		assertEquals(new Integer(2),list.get(0).getId());
		assertEquals(new Integer(5),list.get(1).getId());
		
		list = tq.getPersonOrderByNameDesc(qo);
		assertEquals(new Integer(5),list.get(0).getId());
		assertEquals(new Integer(2),list.get(1).getId());	
	}
	

}
