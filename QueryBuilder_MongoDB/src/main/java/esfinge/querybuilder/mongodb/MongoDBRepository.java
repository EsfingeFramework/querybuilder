package esfinge.querybuilder.mongodb;

import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.annotation.QueryExecutorType;
import esfinge.querybuilder.core.exception.InvalidPropertyException;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

@QueryExecutorType("MONGODB")
public class MongoDBRepository<E> implements Repository<E> {

    protected Datastore ds;
    protected Class<E> configuredClass;

    public MongoDBRepository() {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        ds = dsp.getDatastore();
    }

    @Override
    public void configureClass(Class<E> clazz) {
        configuredClass = clazz;
    }

    @Override
    public E save(E obj) {
        ds.save(obj);
        return obj;
    }

    @Override
    public void delete(Object id) {
        var key = new Key<>(configuredClass, null, id);
        var e = ds.getByKey(configuredClass, key);
        ds.delete(e);
    }

    @Override
    public List<E> list() {
        return ds.find(configuredClass).asList();
    }

    @Override
    public E getById(Object id) {
        var key = new Key<>(configuredClass, null, id);
        return ds.getByKey(configuredClass, key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> queryByExample(E obj) {
        var clazzObj = obj.getClass();
        var query = ds.createQuery(clazzObj);
        try {
            for (var m : clazzObj.getMethods()) {
                if (!m.getName().equals("getClass")
                        && MongoDBDAOUtils.isGetterWhichIsNotTransient(m, clazzObj)) {
                    var value = m.invoke(obj);
                    if (value != null && !value.toString().trim().equals("")) {
                        var prop = m.getName().substring(3, 4).toLowerCase()
                                + m.getName().substring(4);
                        query.field(prop).equal(value);
                    }

                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e) {
            throw new InvalidPropertyException("Error building query", e);
        }
        return (List<E>) query.asList();
    }

}
