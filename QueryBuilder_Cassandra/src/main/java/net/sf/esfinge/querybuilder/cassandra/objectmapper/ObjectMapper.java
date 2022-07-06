package net.sf.esfinge.querybuilder.cassandra.objectmapper;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper<E> {

    MappingManager manager;
    private final Session session;

    private Class<E> clazz;

    public ObjectMapper(Session session, Class<E> clazz) {
        this.session = session;
        this.clazz = clazz;

        checkClassConfiguration();
    }

    private void checkClassConfiguration() {
        if (!clazz.isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("@Table annotation missing from class " + clazz.getSimpleName());

        if (clazz.getDeclaredAnnotation(Table.class).keyspace().equals(""))
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + clazz.getSimpleName());
    }

    public List<E> selectAll() {
        loadManager();

        String keySpaceName = clazz.getDeclaredAnnotation(Table.class).keyspace();

        Mapper<E> mapper = manager.mapper(clazz);

        ResultSet results = session.execute("SELECT * FROM " + keySpaceName + "." + clazz.getSimpleName());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

    private void loadManager(){
        if (manager == null)
            this.manager = new MappingManager(session);
    }
}
