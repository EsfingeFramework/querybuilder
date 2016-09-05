package net.sf.esfinge.querybuilder.jdbc.domainterms;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.jdbc.testresources.Person;
import net.sf.esfinge.querybuilder.jdbc.util.DataBaseCommunicator;

public class QueryBuilderJDBCDomainTermsTest {

	private TestDomainQuery tq = QueryBuilder.create(TestDomainQuery.class);

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator().initializeDatabase("/initial_db_domain.xml");
	}

	@Test
	public void domainUnitQuery() {
		List<Person> list = tq.getPersonById(new Integer(1));
		assertEquals("The list should have 1 persons", 1, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0)
				.getName().trim());
	}

	@Test
	public void domainQuery() {
		List<Person> list = tq.getPersonTeenager();
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0)
				.getName().trim());
		assertEquals("The list should have Maria", "Maria", list.get(1)
				.getName().trim());
	}

	@Test
	public void onDomainTermsQuery() {
		List<Person> list = tq.getPersonPaulista();
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have Maria", "Maria", list.get(0)
				.getName().trim());
		assertEquals("The list should have Marcos", "Marcos", list.get(1)
				.getName().trim());
	}

	@Test
	public void twoDomainTerms() {
		List<Person> list = tq.getPersonTeenagerPaulista();
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Maria", "Maria", list.get(0)
				.getName().trim());
	}

	@Test
	public void domainTermWithTwoWords() {
		List<Person> list = tq.getPersonOldGuys();
		assertEquals("The list should have 2 person", 2, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0)
				.getName().trim());
		assertEquals("The list should have Silvia", "Silvia", list.get(1)
				.getName().trim());
	}

	@Test
	public void domainTermWithParameter() {
		List<Person> list = tq.getPersonPaulistaByAge(20);
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0)
				.getName().trim());
	}

}
