package net.sf.esfinge.querybuilder.neo4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Neo4jSession;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@ServicePriority(1)
public class Neo4JRepository<E> implements Repository<E> {
	
    public static final int DEPTH_LIST = 0;
    public static final int DEPTH_ENTITY = 1;

    protected Neo4jSession neo4j;
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
		neo4j.delete(getById(id));
	}

	@Override
	public List<E> list() {
		return new ArrayList<E>(neo4j.loadAll(clazz, DEPTH_LIST));
	}

	@Override
	public E getById(Object id) {
		return neo4j.load(clazz, (Serializable) id, DEPTH_ENTITY);
	}

	@Override
	public void configureClass(Class<E> clazz) {
		this.clazz = clazz;		
	}

	@Override
	public List<E> queryByExample(E obj) {
        try {
        	Filters filters = new Filters();
        	
            for (Method m : clazz.getMethods()) {
                if (!m.getName().equals("getClass") && Neo4JDAOUtils.isGetter(m, clazz)) {
                    Object value = m.invoke(obj);
                    
                    if (notEmpty(value)) {
                    	String prop = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
                    	filters.add(new Filter(prop, ComparisonOperator.EQUALS, value));
                    }

                }
            }
            
            return new ArrayList<E>(neo4j.loadAll(clazz, filters));
            
        } catch (Exception e) {
            throw new InvalidPropertyException("Error building query", e);
        }
        
    }

	private boolean notEmpty(Object value) {
		return value != null && !value.toString().trim().equals("");
	}

}
