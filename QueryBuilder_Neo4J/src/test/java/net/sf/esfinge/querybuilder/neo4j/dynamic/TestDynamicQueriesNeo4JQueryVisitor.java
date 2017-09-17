package net.sf.esfinge.querybuilder.neo4j.dynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import net.sf.esfinge.querybuilder.neo4j.Neo4JQueryParameters;
import net.sf.esfinge.querybuilder.neo4j.Neo4JVisitorFactory;
import net.sf.esfinge.querybuilder.neo4j.TestNeo4JDatastoreProvider;
import net.sf.esfinge.querybuilder.neo4j.TestQuery;
import net.sf.esfinge.querybuilder.neo4j.domain.Person;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@SuppressWarnings("rawtypes")
public class TestDynamicQueriesNeo4JQueryVisitor {
	
	QueryVisitor visitor;
	MethodParser mp = new DSLMethodParser();
	Neo4jSession neo4j = new TestNeo4JDatastoreProvider().getDatastore();
	
	@Before
	public void init(){
		mp.setInterface(TestQuery.class);
		mp.setEntityClassProvider(ServiceLocator.getServiceImplementation(EntityClassProvider.class));
	}
	
	@Test
	public void notDynamicQuery(){
		
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
		
		assertFalse("Query should not be dynamic", qr.isDynamic());

		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'nome' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	}
	
	@Test
	public void ignoreWhenNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByAge", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		assertEquals("MATCH (n:`Person`) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ]", query);
		
		args[0] = new Integer(15);
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` = 15 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void compareToNullQuery(){
		
		Method m = null;
		try {
			Class[] args = new Class[1];
			args[0] = String.class;
			m = TestQuery.class.getMethod("getPersonByLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[1];
		args[0] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`lastName` IS NULL WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = "Fonseca";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`lastName` = 'Fonseca' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}
	
	@Test
	public void twoCompareToNullQuery(){
		
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
		args[0] = null;
		args[1] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);

		assertEquals("MATCH (n:`Person`) WHERE n.`name` IS NULL AND n.`lastName` IS NULL WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	
		args[0] = "Eduardo";
		args[1] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' AND n.`lastName` IS NULL WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = null;
		args[1] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` IS NULL AND n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = "Eduardo";
		args[1] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' AND n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	
	}
	
	@Test
	public void startsAndCompareToNull(){
		Method m = null;
		try {
			Class[] args = new Class[2];
			args[0] = String.class;
			args[1] = Integer.class;
			m = TestQuery.class.getMethod("getPersonByNameAndAge", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[2];
		args[0] = "M";
		args[1] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` STARTS WITH 'M' AND n.`age` IS NULL WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	
		args[0] = "Eduardo";
		args[1] = 15;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` STARTS WITH 'Eduardo' AND n.`age` = 15 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	
	}
	
	@Test
	public void twoIgnoreWhenNullQuery(){
		
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
		args[0] = null;
		args[1] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertEquals("MATCH (n:`Person`) WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ]", query);
	
		args[0] = "Eduardo";
		args[1] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = null;
		args[1] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = "Eduardo";
		args[1] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' OR n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
	
	}
	
	@Test
	public void twoIgnoreWhenNullQueryPlusOther(){
		
		Method m = null;
		try {
			Class[] args = new Class[3];
			args[0] = String.class;
			args[1] = Integer.class;
			args[2] = String.class;
			m = TestQuery.class.getMethod("getPersonByNameAndAgeAndLastName", args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		Object[] args = new Object[3];
		args[0] = null;
		args[1] = new Integer(15);
		args[2] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		Neo4JQueryParameters neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		String query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		assertEquals("MATCH (n:`Person`) WHERE n.`age` = 15 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = "Eduardo";
		args[1] = new Integer(15);
		args[2] = null;
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' AND n.`age` = 15 WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = null;
		args[1] = new Integer(15);
		args[2] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`age` = 15 AND n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
		args[0] = "Eduardo";
		args[1] = new Integer(15);
		args[2] = "Guerra";
		
		visitor = Neo4JVisitorFactory.createQueryVisitor(mp.parse(m), args);
		
		qr = visitor.getQueryRepresentation();
		neo4jQuery = (Neo4JQueryParameters) qr.getQuery();
		query =  neo4jQuery.resolveQuery(Person.class, neo4j);
		
		assertTrue("Query should be dynamic", qr.isDynamic());
		
		assertEquals("MATCH (n:`Person`) WHERE n.`name` = 'Eduardo' AND n.`age` = 15 AND n.`lastName` = 'Guerra' WITH n RETURN n,[ [ (n)-[r_l1:`LIVES`]->(a1:`Address`) | [ r_l1, a1 ] ] ], ID(n)", query);
		
	}

}
