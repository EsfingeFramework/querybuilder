package org.esfinge.querybuilder.neo4j;

import org.esfinge.querybuilder.annotation.ServicePriority;
import org.esfinge.querybuilder.methodparser.EntityClassProvider;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.utils.ServiceLocator;

@ServicePriority(1)
public class Neo4JEntityClassProvider implements EntityClassProvider {

	@Override
	public Class<?> getEntityClass(String name) {
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		Neo4J neo = dsp.getDatastore();
		return neo.getEntityClass(name);
	}

}
