package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.NotEnoughExamplesException;
import net.sf.esfinge.querybuilder.cassandra.reflection.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CassandraRepository<E> implements Repository<E> {

    private CassandraSessionProvider client;
    private Session session;
    protected Class<E> clazz;
    private MappingManager manager;

    public CassandraRepository() {
        this.client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
    }

    private void loadManager(){
        if (manager == null){
            this.session = client.getSession();
            this.manager = new MappingManager(session);
        }
    }

    @Override
    public E save(E e) {
        loadManager();

        Mapper<E> mapper = manager.mapper(clazz);
        mapper.save(e);

        return e;
    }

    @Override
    public void delete(Object id) {
        loadManager();

        Mapper<E> mapper = manager.mapper(clazz);
        mapper.delete(id);
    }

    @Override
    public List<E> list() {
        loadManager();

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
    public E getById(Object id) {
        loadManager();

        Mapper<E> mapper = manager.mapper(clazz);

        return mapper.get(id);
    }

    @Override
    public List<E> queryByExample(E e) {
        loadManager();

        Method[] getters = ReflectionUtils.getClassGetters(e.getClass());

        if (getters.length == 0)
            throw new NotEnoughExamplesException("At least one attribute needed for class " + e.getClass());

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM ").append(clazz.getDeclaredAnnotation(Table.class).keyspace()).append(".").append(clazz.getSimpleName()).append(" WHERE ");

        for (int i = 0; i < getters.length; i++) {
            try {
                if (getters[i].invoke(e) != null) {
                    if (i > 0)
                        queryBuilder.append(" AND ");

                    queryBuilder.append(getters[i].getName().substring(3).toLowerCase()).append(" = ");

                    if (getters[i].getReturnType() == String.class)
                        queryBuilder.append("'").append(getters[i].invoke(e)).append("'");
                    else
                        queryBuilder.append(getters[i].invoke(e));
                }


            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        queryBuilder.append(" ALLOW FILTERING");

        Mapper<E> mapper = manager.mapper(clazz);

        ResultSet results = session.execute(queryBuilder.toString());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    @Override
    public void configureClass(Class<E> aClass) {
        checkValidClassConfiguration(aClass);
        this.clazz = aClass;
    }

    private void checkValidClassConfiguration(Class<E> aClass) {
        if (!aClass.isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("@Table annotation missing from class " + aClass.getSimpleName());

        Field[] classFields = aClass.getDeclaredFields();
        boolean partitionAnnotationFound = false;

        for (Field f : classFields) {
            if (f.isAnnotationPresent(PartitionKey.class)) {
                partitionAnnotationFound = true;
                break;
            }
        }

        if (!partitionAnnotationFound)
            throw new MissingAnnotationException("The @PartitionKey annotation needs to be assigned to a field " + aClass.getSimpleName());

        if (aClass.getDeclaredAnnotation(Table.class).keyspace().equals(""))
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + aClass.getSimpleName());
    }
}
