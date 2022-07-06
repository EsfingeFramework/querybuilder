package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class CassandraRepository<E> implements Repository<E> {
    protected Class<E> clazz;
    private final Session session;

    public CassandraRepository() {
        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        this.session = client.getSession();
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
        if (!clazz.isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("@Table annotation missing from class " + clazz.getSimpleName());

        if (clazz.getDeclaredAnnotation(Table.class).keyspace().equals(""))
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + clazz.getSimpleName());

        MappingManager manager = new MappingManager(session);

        System.out.println(clazz);
        Mapper<E> mapper = manager.mapper(clazz);

        ResultSet results = session.execute("SELECT * FROM " + clazz.getDeclaredAnnotation(Table.class).keyspace() + "." + clazz.getSimpleName());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
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
