package net.sf.esfinge.querybuilder.neo4j;

import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;

@ServicePriority(0)
public class TestNeo4JDatastoreProvider implements DatastoreProvider {
	
	private final static SessionFactory sessionFactory = new SessionFactory(
//			new Builder().uri("bolt://localhost").credentials("neo4j", "admin").connectionPoolSize(150).build(), // comment to use EmbeddedDriver
			"net.sf.esfinge.querybuilder.neo4j.domain");
	
	private static Neo4jSession neo4j = (Neo4jSession) sessionFactory.openSession();
	
	@Override
	public Neo4jSession getDatastore() {
		return neo4j;
	}

}
