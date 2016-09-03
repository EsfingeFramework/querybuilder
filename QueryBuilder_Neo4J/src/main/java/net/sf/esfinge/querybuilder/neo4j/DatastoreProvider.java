package net.sf.esfinge.querybuilder.neo4j;

import net.sf.esfinge.querybuilder.neo4j.oomapper.Neo4J;

public interface DatastoreProvider {
	
	public Neo4J getDatastore();
	
}
