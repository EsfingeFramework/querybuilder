package ef.qb.mongodb;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import ef.qb.core.Repository;
import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.exception.InvalidPropertyException;
import ef.qb.core.utils.ServiceLocator;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
    public void delete(E obj) {
        ds.delete(obj);
    }

    @Override
    public List<E> list() {
        return ds.find(configuredClass).iterator().toList();  // `asList()` foi substituído por `iterator().toList()`
    }

    @Override
    public E getById(Object id) {
        return ds.find(configuredClass).filter(Filters.eq("_id", id)).first();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> queryByExample(E obj) {
        var clazzObj = obj.getClass();
        var query = (Query<E>) ds.find(clazzObj);

        try {
            for (var m : clazzObj.getMethods()) {
                if (!m.getName().equals("getClass") && MongoDBDAOUtils.isGetterWhichIsNotTransient(m, clazzObj)) {
                    var value = m.invoke(obj);
                    if (value != null && !value.toString().trim().equals("")) {
                        var prop = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
                        query.filter(Filters.eq(prop, value));
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e) {
            throw new InvalidPropertyException("Error building query", e);
        }
        return (List<E>) query.iterator().toList();
    }
}
