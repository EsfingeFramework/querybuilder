package ef.qb.cassandra;

import com.datastax.driver.mapping.annotations.Table;
import static ef.qb.cassandra.cassandrautils.CassandraUtils.checkValidClassConfiguration;
import ef.qb.cassandra.cassandrautils.MappingManagerProvider;
import ef.qb.cassandra.exceptions.NotEnoughExamplesException;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import ef.qb.core.Repository;
import ef.qb.core.annotation.QueryExecutorType;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@QueryExecutorType(CASSANDRA)
public class CassandraRepository<E> implements Repository<E> {

    protected Class<E> clazz;
    MappingManagerProvider provider;

    public CassandraRepository() {
        provider = new MappingManagerProvider();
    }

    @Override
    public E save(E e) {
        var mapper = provider.getManager().mapper(clazz);
        mapper.save(e);

        return e;
    }

    @Override
    public void delete(E e) {
        try {
            var mapper = provider.getManager().mapper(clazz);
            var getIdMethod = e.getClass().getMethod("getId");
            var idValue = (UUID) getIdMethod.invoke(e);
            mapper.delete(idValue);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CassandraRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<E> list() {
        var mapper = provider.getManager().mapper(clazz);
        var results = provider.getSession().execute("SELECT * FROM " + clazz.getDeclaredAnnotation(Table.class).keyspace() + "." + clazz.getSimpleName());
        var objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (var u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    @Override
    public E getById(Object id) {
        var mapper = provider.getManager().mapper(clazz);

        return mapper.get(id);
    }

    @Override
    public List<E> queryByExample(E e) {
        var getters = getClassGetters(e.getClass());

        if (getters.length == 0) {
            throw new NotEnoughExamplesException("At least one attribute needed for class " + e.getClass());
        }

        var queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM ").append(clazz.getDeclaredAnnotation(Table.class).keyspace()).append(".").append(clazz.getSimpleName()).append(" WHERE ");

        var conditions = 0;
        for (var ter : getters) {
            try {
                if (ter.invoke(e) != null) {
                    if (conditions > 0) {
                        queryBuilder.append(" AND ");
                    }
                    queryBuilder.append(ter.getName().substring(3).toLowerCase()).append(" = ");
                    conditions++;
                    if (ter.getReturnType() == String.class) {
                        queryBuilder.append("'").append(ter.invoke(e)).append("'");
                    } else {
                        queryBuilder.append(ter.invoke(e));
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        queryBuilder.append(" ALLOW FILTERING");

        var mapper = provider.getManager().mapper(clazz);
        var results = provider.getSession().execute(queryBuilder.toString());
        var objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (var u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    @Override
    public void configureClass(Class<E> aClass) {
        checkValidClassConfiguration(aClass);
        this.clazz = aClass;
    }

}
