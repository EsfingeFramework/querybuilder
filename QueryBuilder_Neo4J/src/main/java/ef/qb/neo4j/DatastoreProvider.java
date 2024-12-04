package ef.qb.neo4j;

import org.neo4j.ogm.session.Neo4jSession;

public interface DatastoreProvider {
	
	public Neo4jSession getDatastore();
	
}
