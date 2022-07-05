package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.EntityRepository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

import java.util.List;

public class CassandraRepository<E> implements Repository<E> {

    private String keyspaceName;
    private KeyspaceRepository schemaRepository;
    private EntityRepository entityRepository;
    private Session session;

    protected Class<E> clazz;

    public CassandraRepository() {
        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        this.session = client.getSession();
        this.keyspaceName = client.getKeyspaceName();

        this.schemaRepository = new KeyspaceRepository(session);
        this.entityRepository = new EntityRepository(session);
    }

    @Override
    public E save(E e) {
        return null;
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public List<E> list() {
        return entityRepository.selectAll(clazz,keyspaceName);
    }

    @Override
    public E getById(Object o) {
        return null;
    }

    @Override
    public List<E> queryByExample(E e) {
        return null;
    }

    @Override
    public void configureClass(Class<E> aClass) {
        this.clazz = aClass;
    }
}
