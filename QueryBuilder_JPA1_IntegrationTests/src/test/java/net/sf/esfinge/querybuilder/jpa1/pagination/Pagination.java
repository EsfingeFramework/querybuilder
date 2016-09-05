package net.sf.esfinge.querybuilder.jpa1.pagination;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.jpa1.testresources.Person;
import net.sf.esfinge.querybuilder.jpa1.util.QueryBuilderDatabaseTest;

public class Pagination extends QueryBuilderDatabaseTest {

	private PersonPaginationQueries dao;

	@Before
	public void init() throws Exception {
		initializeDatabase("/initial_db_pagination.xml");
		dao = QueryBuilder.create(PersonPaginationQueries.class);
	}
	
	@Test
	public void paginationPageNumberInvariablePageSize() throws Exception {
		assertEquals(7, dao.getPerson(1).size());
		assertEquals(5, dao.getPerson(2).size());
		assertEquals(0, dao.getPerson(3).size());
		
		assertEquals(1, dao.getPersonByName("Mar", 1).size());
		assertEquals(1, dao.getPersonByName("Mar", 2).size());
		assertEquals(1, dao.getPersonByName("Mar", 3).size());
		assertEquals(0, dao.getPersonByName("Mar", 4).size());
		
		assertEquals(2, dao.getPersonByAddressCity("SJCampos", 1).size());
		assertEquals(2, dao.getPersonByAddressCity("SJCampos", 2).size());
		assertEquals(0, dao.getPersonByAddressCity("SJCampos", 3).size());
	}

	@Test
	public void paginationPageNumberInvariablePageSizeDomainTerm() throws Exception {
		assertEquals(2, dao.getPersonTeenager(1).size());
		assertEquals(2, dao.getPersonTeenager(2).size());
		assertEquals(1, dao.getPersonTeenager(3).size());
		assertEquals(0, dao.getPersonTeenager(4).size());
	}

	@Test
	public void paginationPageNumberInvariablePageSizeOrderByName() throws Exception {
		Collection<Person> pageOne = dao.getPersonOrderByName(1);
		String[] pageOneResponse = { "Alessandra", "Eduardo", "Gabriela", "Guilherme" };

		assertEquals(4, pageOne.size());

		int index = 0;
		for (Person person : pageOne) {
			assertEquals(pageOneResponse[index++], person.getName());
		}

		Collection<Person> pageTwo = dao.getPersonOrderByName(2);
		String[] pageTwoResponse = { "Joao", "Marcia", "Marcos", "Maria" };

		assertEquals(4, pageTwo.size());

		index = 0;
		for (Person person : pageTwo) {
			assertEquals(pageTwoResponse[index++], person.getName());
		}

		Collection<Person> pageTree = dao.getPersonOrderByName(3);
		String[] pageTreeResponse = { "Pedro", "Rogerio", "Rose", "Silvia" };

		assertEquals(4, pageTree.size());

		index = 0;
		for (Person person : pageTree) {
			assertEquals(pageTreeResponse[index++], person.getName());
		}
	}

	@Test
	public void paginationPageNumberInvariablePageSizeOrderByNameDesc() throws Exception {
		Collection<Person> pageOne = dao.getPersonOrderByNameDesc(1);
		String[] pageOneResponse = { "Silvia", "Rose", "Rogerio", "Pedro" };

		assertEquals(4, pageOne.size());

		int index = 0;
		for (Person person : pageOne) {
			assertEquals(pageOneResponse[index++], person.getName());
		}

		Collection<Person> pageTwo = dao.getPersonOrderByNameDesc(2);
		String[] pageTwoResponse = { "Maria", "Marcos", "Marcia", "Joao" };

		assertEquals(4, pageTwo.size());

		index = 0;
		for (Person person : pageTwo) {
			assertEquals(pageTwoResponse[index++], person.getName());
		}

		Collection<Person> pageTree = dao.getPersonOrderByNameDesc(3);
		String[] pageTreeResponse = { "Guilherme", "Gabriela", "Eduardo", "Alessandra" };

		assertEquals(4, pageTree.size());

		index = 0;
		for (Person person : pageTree) {
			assertEquals(pageTreeResponse[index++], person.getName());
		}
	}

	@Test
	public void paginationPageNumberInvariablePageSizeQueryObject() throws Exception {
		PersonQuery personQuery = new PersonQuery();
		personQuery.setAge(17);

		assertEquals(3, dao.getPerson(personQuery, 1).size());
		assertEquals(3, dao.getPerson(personQuery, 2).size());
		assertEquals(1, dao.getPerson(personQuery, 3).size());
		assertEquals(0, dao.getPerson(personQuery, 4).size());
	}
	
	
	
	
	
	@Test
	public void paginationPageNumberVariablePageSize() throws Exception {
		assertEquals(7, dao.getPerson(1, 7).size());
		assertEquals(5, dao.getPerson(2, 7).size());
		assertEquals(0, dao.getPerson(3, 7).size());
		
		assertEquals(1, dao.getPersonByName("Mar", 1, 1).size());
		assertEquals(1, dao.getPersonByName("Mar", 2, 1).size());
		assertEquals(1, dao.getPersonByName("Mar", 3, 1).size());
		assertEquals(0, dao.getPersonByName("Mar", 4, 1).size());
		
		assertEquals(2, dao.getPersonByAddressCity("SJCampos", 1, 2).size());
		assertEquals(2, dao.getPersonByAddressCity("SJCampos", 2, 2).size());
		assertEquals(0, dao.getPersonByAddressCity("SJCampos", 3, 2).size());
	}

	@Test
	public void paginationPageNumberVariablePageSizeDomainTerm() throws Exception {
		assertEquals(2, dao.getPersonTeenager(1, 2).size());
		assertEquals(2, dao.getPersonTeenager(2, 2).size());
		assertEquals(1, dao.getPersonTeenager(3, 2).size());
		assertEquals(0, dao.getPersonTeenager(4, 2).size());
	}

	@Test
	public void paginationPageNumberVariablePageSizeOrderByName() throws Exception {
		Collection<Person> pageOne = dao.getPersonOrderByName(1, 4);
		String[] pageOneResponse = { "Alessandra", "Eduardo", "Gabriela", "Guilherme" };

		assertEquals(4, pageOne.size());

		int index = 0;
		for (Person person : pageOne) {
			assertEquals(pageOneResponse[index++], person.getName());
		}

		Collection<Person> pageTwo = dao.getPersonOrderByName(2);
		String[] pageTwoResponse = { "Joao", "Marcia", "Marcos", "Maria" };

		assertEquals(4, pageTwo.size());

		index = 0;
		for (Person person : pageTwo) {
			assertEquals(pageTwoResponse[index++], person.getName());
		}

		Collection<Person> pageTree = dao.getPersonOrderByName(3);
		String[] pageTreeResponse = { "Pedro", "Rogerio", "Rose", "Silvia" };

		assertEquals(4, pageTree.size());

		index = 0;
		for (Person person : pageTree) {
			assertEquals(pageTreeResponse[index++], person.getName());
		}
	}

	@Test
	public void paginationPageNumberVariablePageSizeOrderByNameDesc() throws Exception {
		Collection<Person> pageOne = dao.getPersonOrderByNameDesc(1, 4);
		String[] pageOneResponse = { "Silvia", "Rose", "Rogerio", "Pedro" };

		assertEquals(4, pageOne.size());

		int index = 0;
		for (Person person : pageOne) {
			assertEquals(pageOneResponse[index++], person.getName());
		}

		Collection<Person> pageTwo = dao.getPersonOrderByNameDesc(2);
		String[] pageTwoResponse = { "Maria", "Marcos", "Marcia", "Joao" };

		assertEquals(4, pageTwo.size());

		index = 0;
		for (Person person : pageTwo) {
			assertEquals(pageTwoResponse[index++], person.getName());
		}

		Collection<Person> pageTree = dao.getPersonOrderByNameDesc(3);
		String[] pageTreeResponse = { "Guilherme", "Gabriela", "Eduardo", "Alessandra" };

		assertEquals(4, pageTree.size());

		index = 0;
		for (Person person : pageTree) {
			assertEquals(pageTreeResponse[index++], person.getName());
		}
	}

	@Test
	public void paginationPageNumberVariablePageSizeQueryObject() throws Exception {
		PersonQuery personQuery = new PersonQuery();
		personQuery.setAge(17);

		assertEquals(3, dao.getPerson(personQuery, 1, 3).size());
		assertEquals(3, dao.getPerson(personQuery, 2, 3).size());
		assertEquals(1, dao.getPerson(personQuery, 3, 3).size());
		assertEquals(0, dao.getPerson(personQuery, 4, 3).size());
	}

}
