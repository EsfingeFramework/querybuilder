package org.esfinge.querybuilder.mongodb;

import java.lang.reflect.Method;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class MongoDBRepository<E> implements Repository<E>{
	
	protected Datastore ds;
	protected Class<E> clazz;
	
	public MongoDBRepository(){
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		ds = dsp.getDatastore();
	}

	@Override
	public E save(E obj) {
		ds.save(obj);
		return obj;
	}

	@Override
	public void delete(Object id) {
		Key<E> key = new Key<E>(clazz, id);
		E e = ds.getByKey(clazz, key);
		ds.delete(e);
	}

	@Override
	public List<E> list() {
		return ds.find(clazz).asList();
	}

	@Override
	public E getById(Object id) {
		Key<E> key = new Key<E>(clazz, id);
		return ds.getByKey(clazz, key);
	}

	@Override
	public void configureClass(Class<E> clazz) {
		this.clazz = clazz;		
	}

	@Override
	public List<E> queryByExample(E obj) {
		
		Class<?> clazz = obj.getClass();
        Query query = ds.createQuery(clazz);
        
        try {
            for (Method m : clazz.getMethods()) {
                if (!m.getName().equals("getClass")
                        && MongoDBDAOUtils.isGetterWhichIsNotTransient(m, clazz)) {
                    Object value = m.invoke(obj);
                    if (value != null && !value.toString().trim().equals("")) {
                        String prop = m.getName().substring(3, 4).toLowerCase()
                                + m.getName().substring(4);
                        query.field(prop).equal(value);
                    }

                }
            }
        } catch (Exception e) {
            throw new InvalidPropertyException("Error building query", e);
        }

        return query.asList();
        
    }

}
