package org.esfinge.querybuilder.neo4j;

import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.querybuilder.neo4j.oomapper.MappingInfo;
import org.esfinge.querybuilder.neo4j.oomapper.Neo4J;
import org.esfinge.querybuilder.neo4j.oomapper.Query;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@ServicePriority(1)
public class Neo4JRepository<E> implements Repository<E>{
	
	protected Neo4J neo4j;
	protected Class<E> clazz;
	
	public Neo4JRepository(){
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		neo4j = dsp.getDatastore();
	}

	@Override
	public E save(E obj) {
		neo4j.save(obj);
		return obj;
	}

	@Override
	public void delete(Object id) {
		neo4j.delete(clazz, id);
	}

	@Override
	public List<E> list() {
		Query<E> q = neo4j.query(clazz);
		return q.asList();
	}

	@Override
	public E getById(Object id) {
		Query<E> q = neo4j.query(clazz);
		MappingInfo info = neo4j.getMappingInfo(clazz);
		q.setProperty(info.getId(), id);
		return q.getSingle();
	}

	@Override
	public void configureClass(Class<E> clazz) {
		this.clazz = clazz;		
	}

	@Override
	public List<E> queryByExample(E obj) {
		
		Class<?> clazz = obj.getClass();
        Query<E> query = neo4j.query(clazz);
        
        try {
            for (Method m : clazz.getMethods()) {
                if (!m.getName().equals("getClass") && Neo4JDAOUtils.isGetter(m, clazz)) {
                    Object value = m.invoke(obj);
                    
                    if (value != null && !value.toString().trim().equals("")) {
                        String prop = m.getName().substring(3, 4).toLowerCase()
                                + m.getName().substring(4);
                        query.setProperty(prop, value);
                    }

                }
            }
        } catch (Exception e) {
            throw new InvalidPropertyException("Error building query", e);
        }

        return query.asList();
        
    }

}
