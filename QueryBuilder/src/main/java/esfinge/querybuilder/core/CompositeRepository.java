package esfinge.querybuilder.core;

import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import esfinge.querybuilder.core.methodparser.QueryType;
import java.util.List;
import lombok.Data;

@Data
public class CompositeRepository<E> implements Repository<E> {

    private final Repository<E> primary;
    private final Repository secondary;
    private final QueryExecutor queryExecutor;
    private Class<E> configuredClass;

    public CompositeRepository(Repository<E> primary, Repository secondary, QueryExecutor queryExecutor) {
        this.primary = primary;
        this.secondary = secondary;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public E save(E obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<E> list() {
        var qi = new QueryInfo();
        qi.setEntityType(configuredClass);
        qi.setEntityName(configuredClass.getSimpleName());
        qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        qi.setQueryType(QueryType.RETRIEVE_LIST);
        return (List<E>) queryExecutor.executeQuery(qi, null);
    }

    @Override
    public E getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<E> queryByExample(E obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
