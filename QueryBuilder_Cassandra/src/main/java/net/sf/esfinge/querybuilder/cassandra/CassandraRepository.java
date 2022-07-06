package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.objectmapper.ObjectMapper;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

import java.util.List;

public class CassandraRepository<E> implements Repository<E> {
    protected Class<E> clazz;
    private final ObjectMapper objectMapper;
    private final Session session;

    public CassandraRepository() {
        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        this.session = client.getSession();

        this.objectMapper = new ObjectMapper(session);
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
        return objectMapper.selectAll(clazz);
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
