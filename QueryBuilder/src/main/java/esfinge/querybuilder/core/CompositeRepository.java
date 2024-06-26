package esfinge.querybuilder.core;

import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import esfinge.querybuilder.core.methodparser.QueryType;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_LIST;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_SINGLE;
import java.util.List;

public class CompositeRepository<E> implements Repository<E> {

    private final Repository<E> primaryRepo;
    private final Repository secondaryRepo;
    private final QueryExecutor compQueryExecutor;
    private Class<E> configuredClass;

    @Override
    public void configureClass(Class<E> clazz) {
        configuredClass = clazz;
    }

    public CompositeRepository(Repository<E> primaryRepo, Repository secondaryRepo, QueryExecutor compQueryExecutor) {
        this.primaryRepo = primaryRepo;
        this.secondaryRepo = secondaryRepo;
        this.compQueryExecutor = compQueryExecutor;
    }

    @Override
    public E save(E obj) {
        if (primaryRepo != null) {
            E savedObj = primaryRepo.save(obj);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Object id) {
        if (primaryRepo != null) {
            primaryRepo.delete(id);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<E> list() {
        return (List<E>) compQueryExecutor.executeQuery(createQueryInfo(RETRIEVE_LIST), null);
    }

    @Override
    public E getById(Object id) {
        return (E) compQueryExecutor.executeQuery(createQueryInfo(RETRIEVE_SINGLE), null);
    }

    @Override
    public List<E> queryByExample(E obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private QueryInfo createQueryInfo(QueryType queryType) {
        var qi = new QueryInfo();
        qi.setEntityType(configuredClass);
        qi.setEntityName(configuredClass.getSimpleName());
        qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        qi.setQueryType(queryType);
        return qi;
    }

}
