package esfinge.querybuilder.core;

import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import esfinge.querybuilder.core.methodparser.QueryType;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_LIST;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_SINGLE;
import java.util.List;

public class CompositeRepository<E> implements Repository<E> {

    private final Repository<E> priRepository;
    private final Repository secRepository;
    private final QueryExecutor compExecutor;
    private final List<RelationHandler<E>> relationHandlers;
    private Class<E> configuredClass;

    public CompositeRepository(Repository<E> priRepository, Repository secRepository, QueryExecutor compExecutor) {
        this.priRepository = priRepository;
        this.secRepository = secRepository;
        this.compExecutor = compExecutor;
        this.relationHandlers = List.of(
                new OneToOneLeftHandler<>(),
                new OneToOneRightHandler<>(),
                new OneToManyHandler<>()
        );
    }

    @Override
    public void configureClass(Class<E> clazz) {
        configuredClass = clazz;
    }

    @Override
    public E save(E obj) {
        if (priRepository != null && obj != null) {
            for (var field : obj.getClass().getDeclaredFields()) {
                for (var handler : relationHandlers) {
                    if (handler.supports(field)) {
                        obj = handler.handleSave(field, obj, priRepository, secRepository);
                    }
                }
            }
            return priRepository.save(obj);
        }
        return null;
    }

    @Override
    public void delete(E obj) {
        if (priRepository != null && obj != null) {
            for (var field : obj.getClass().getDeclaredFields()) {
                for (var handler : relationHandlers) {
                    if (handler.supports(field)) {
                        handler.handleDelete(field, obj, secRepository);
                    }
                }
            }
            priRepository.delete(obj);
        }
    }

    @Override
    public List<E> list() {
        return (List<E>) compExecutor.executeQuery(createBasicQueryInfo(RETRIEVE_LIST), null);
    }

    @Override
    public E getById(Object id) {
        return (E) compExecutor.executeQuery(createBasicQueryInfo(RETRIEVE_SINGLE), null);
    }

    @Override
    public List<E> queryByExample(E obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private QueryInfo createBasicQueryInfo(QueryType queryType) {
        var qi = new QueryInfo();
        qi.setEntityType(configuredClass);
        qi.setEntityName(configuredClass.getSimpleName());
        qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        qi.setQueryType(queryType);
        return qi;
    }
}
