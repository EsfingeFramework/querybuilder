package net.sf.esfinge.querybuilder.cassandra.cassandrautils;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class MappingManagerProvider {

    private final CassandraSessionProvider client;
    private Session session;
    private MappingManager manager;

    public MappingManagerProvider() {
        this.client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
    }

    private void loadManager() {
        if (manager == null) {
            this.session = client.getSession();
            this.manager = new MappingManager(session);
        }
    }

    public CassandraSessionProvider getClient() {
        return client;
    }

    public Session getSession() {
        return session;
    }

    public MappingManager getManager() {
        loadManager();
        return manager;
    }
}
