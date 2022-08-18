package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.CassandraUtils;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.MappingManagerProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.NotEnoughExamplesException;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CassandraRepository<E> implements Repository<E> {

    protected Class<E> clazz;
    MappingManagerProvider provider;

    public CassandraRepository() {
        provider = new MappingManagerProvider();
    }

    @Override
    public E save(E e) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);
        mapper.save(e);

        return e;
    }

    @Override
    public void delete(Object id) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);
        mapper.delete(id);
    }

    @Override
    public List<E> list() {
        Mapper<E> mapper = provider.getManager().mapper(clazz);

        ResultSet results = provider.getSession().execute("SELECT * FROM " + clazz.getDeclaredAnnotation(Table.class).keyspace() + "." + clazz.getSimpleName());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    @Override
    public E getById(Object id) {
        Mapper<E> mapper = provider.getManager().mapper(clazz);

        return mapper.get(id);
    }

    @Override
    public List<E> queryByExample(E e) {
        Method[] getters = CassandraReflectionUtils.getClassGetters(e.getClass());

        if (getters.length == 0)
            throw new NotEnoughExamplesException("At least one attribute needed for class " + e.getClass());

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM ").append(clazz.getDeclaredAnnotation(Table.class).keyspace()).append(".").append(clazz.getSimpleName()).append(" WHERE ");

        int conditions = 0;

        for (int i = 0; i < getters.length; i++) {
            try {

                if (getters[i].invoke(e) != null) {

                    if (conditions > 0){
                        queryBuilder.append(" AND ");
                    }

                    queryBuilder.append(getters[i].getName().substring(3).toLowerCase()).append(" = ");
                    conditions++;

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

        Mapper<E> mapper = provider.getManager().mapper(clazz);

        System.out.println(queryBuilder.toString());

        ResultSet results = provider.getSession().execute(queryBuilder.toString());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    @Override
    public void configureClass(Class<E> aClass) {
        CassandraUtils.checkValidClassConfiguration(aClass);
        this.clazz = aClass;
    }

}
