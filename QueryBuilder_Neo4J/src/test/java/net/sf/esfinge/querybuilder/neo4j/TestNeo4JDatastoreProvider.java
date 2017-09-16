package net.sf.esfinge.querybuilder.neo4j;

import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;

@ServicePriority(0)
public class TestNeo4JDatastoreProvider implements DatastoreProvider{
	
	private final static SessionFactory sessionFactory = new SessionFactory("net.sf.esfinge.querybuilder.neo4j.domain");
	private static Neo4jSession neo4j;
	
	public TestNeo4JDatastoreProvider() {
//		neo4j = new Neo4jSession(new MetaData(Person.class, Address.class), new EmbeddedDriver());
		neo4j = (Neo4jSession) sessionFactory.openSession();
	}
	
	@Override
	public Neo4jSession getDatastore() {
		return neo4j;
	}

}
