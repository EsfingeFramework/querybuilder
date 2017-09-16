package net.sf.esfinge.querybuilder.neo4j;

import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.session.Neo4jSession;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@ServicePriority(1)
public class Neo4JEntityClassProvider implements EntityClassProvider {

	private static final Neo4jSession neo = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();

	@Override
	public Class<?> getEntityClass(String name) {
		ClassInfo classInfo = neo.metaData().classInfo(name);
		return (classInfo == null) ? null : classInfo.getUnderlyingClass();
	}

}
