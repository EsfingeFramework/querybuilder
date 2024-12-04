package ef.qb.neo4j;

import ef.qb.core.Repository;
import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.exception.InvalidPropertyException;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import ef.qb.core.utils.ServiceLocator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Neo4jSession;

@ServicePriority(1)
@QueryExecutorType(NEO4J)
public class Neo4JRepository<E> implements Repository<E> {

    private final int DEPTH_ENTITY = 0;
    private final int DEPTH_LIST = 1;

    protected Neo4jSession neo4j;
    protected Class<E> clazz;

    public Neo4JRepository() {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
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
        return new ArrayList<>(neo4j.loadAll(clazz, DEPTH_LIST));
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
            var filters = new Filters();
            for (var m : clazz.getMethods()) {
                if (!m.getName().equals("getClass") && Neo4JDAOUtils.isGetter(m, clazz)) {
                    var value = m.invoke(obj);

                    if (notEmpty(value)) {
                        var prop = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
                        filters.add(new Filter(prop, ComparisonOperator.EQUALS, value));
                    }

                }
            }

            return new ArrayList<>(neo4j.loadAll(clazz, filters));

        } catch (Exception e) {
            throw new InvalidPropertyException("Error building query", e);
        }

    }

    private boolean notEmpty(Object value) {
        return value != null && !value.toString().trim().equals("");
    }

}
