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

// TODO: IMPLEMENT METHODS FOR CREATION AND DELETION OF TABLES AND INSERTION OF DATA

public class ObjectMapper {

    private static final String TABLE_NAME = "books";

    private static final String TABLE_NAME_BY_TITLE = TABLE_NAME + "ByTitle";

    private Session session;
    MappingManager manager;


    public ObjectMapper(Session session) {
        this.session = session;
        this.manager = new MappingManager(session);
    }

    public <E> List<E> selectAll(Class<E> clazz) {
        if (!clazz.isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("@Table annotation missing from class " + clazz.getSimpleName());

        String keySpaceName = clazz.getDeclaredAnnotation(Table.class).keyspace();

        if (keySpaceName.equals(""))
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + clazz.getSimpleName());

        Mapper<E> mapper = manager.mapper(clazz);

        ResultSet results = session.execute("SELECT * FROM " + keySpaceName + "." + clazz.getSimpleName());
        Result<E> objects = mapper.map(results);
        List<E> objectsList = new ArrayList<>();

        for (E u : objects) {
            objectsList.add(u);
        }

        return objectsList;
    }

}
