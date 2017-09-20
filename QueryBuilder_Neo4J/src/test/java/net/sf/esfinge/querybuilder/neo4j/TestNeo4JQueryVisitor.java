package net.sf.esfinge.querybuilder.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.session.Neo4jSession;

import net.sf.esfinge.querybuilder.methodparser.DSLMethodParser;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.methodparser.MethodParser;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.neo4j.domain.Person;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@SuppressWarnings("rawtypes")
public class TestNeo4JQueryVisitor {
	
	QueryVisitor visitor;
	MethodParser mp = new DSLMethodParser();
	Neo4jSession neo4j = new TestNeo4JDatastoreProvider().getDatastore();
	
	@Before
	public void init(){
		mp.setInterface(TestQuery.class);
		mp.setEntityClassProvider(ServiceLocator.getServiceImplementation(EntityClassProvider.class));
	}
	
	@Test
	public void singleEntity() throws NoSuchMethodException, SecurityException {
		
		Method m = TestQuery.class.getMethod("getPerson", new Class[0]);

		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), null);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ]", query);
	}
	
	@Test
	public void oneCondition(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("nome");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void greater(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAgeGreater", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new Integer(10);
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` > 10 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void greaterEquals(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = Integer.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByAgeAndName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new Integer(10);
		args[1] = "John";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` >= 10 AND n.`name` = 'John' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void greaterNameDesc(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAgeOrderByNameDesc", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new Integer(10);
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` > 10 WITH n ORDER BY n.name DESC RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void greaterFluent(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAgeGreater", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new Integer(10);
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` > 10 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void notEquals(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameNotEquals", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = "John";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE NOT(n.`name` = 'John' ) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void twoConditionsAND(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameAndLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' AND n.`lastName` = 'sobrenome' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void twoConditionsOR(){
		
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameOrLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);

		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' OR n.`lastName` = 'sobrenome' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void compositeCondition(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByAddressCity", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("cidade");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) MATCH (m0:`Address`) WHERE m0.`city` = 'cidade' MATCH (n)-[:`LIVES`]->(m0) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void complexQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[3];
			args[0] = String.class;
			args[1] = String.class;
			args[2] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameOrLastNameAndAddressCity", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[3];
		args[0] = new String("nome");
		args[1] = new String("sobrenome");
		args[2] = new String("cidade");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' OR n.`lastName` = 'sobrenome' MATCH (m0:`Address`) WHERE m0.`city` = 'cidade' MATCH (n)-[:`LIVES`]->(m0) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void fixParameterQuery(){
		
		Method m = null;
		try {
			m = TestQuery.class.getMethod("getPersonCarioca");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[0];
				
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m),args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) MATCH (m0:`Address`) WHERE m0.`city` = 'Rio de Janeiro' MATCH (n)-[:`LIVES`]->(m0) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		assertEquals(qr.getFixParameterValue("cityEQUALS"), "Rio de Janeiro");
		assertTrue(qr.getFixParameters().contains("cityEQUALS"));
	}
	
	@Test
	public void mixedWithfixParameterQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonCariocaByName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = new String("nome");
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' MATCH (m0:`Address`) WHERE m0.`city` = 'Rio de Janeiro' MATCH (n)-[:`LIVES`]->(m0) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		assertEquals(qr.getFixParameterValue("cityEQUALS"), "Rio de Janeiro");
		assertTrue(qr.getFixParameters().contains("cityEQUALS"));
	}
	
}
