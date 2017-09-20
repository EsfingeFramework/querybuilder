package net.sf.esfinge.querybuilder.neo4j.testresources;

import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.neo4j.DatastoreProvider;

@ServicePriority(1)
public class TestNeo4JDatastoreProvider implements DatastoreProvider {
	
	private static SessionFactory sessionFactory = new SessionFactory(
//			new Builder().uri("bolt://localhost").credentials("neo4j", "admin").connectionPoolSize(150).build(), // comment to use EmbeddedDriver
			"net.sf.esfinge.querybuilder.neo4j.domain");
	private static Neo4jSession neo4j = (Neo4jSession) sessionFactory.openSession();

	
	public void clear(){
		neo4j.purgeDatabase();
	}

	@Override
	public Neo4jSession getDatastore() {
		return neo4j;
	}

	public Class<?> getEntityClass(String tableName) {
		String name = tableName.substring(0, 1) + tableName.substring(1).toLowerCase();
		ClassInfo classInfo = neo4j.metaData().classInfo(name);
		return (classInfo == null) ? null : classInfo.getUnderlyingClass();
	}
}
