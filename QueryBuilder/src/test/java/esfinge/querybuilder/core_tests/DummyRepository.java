package esfinge.querybuilder.core_tests;

import esfinge.querybuilder.core.Repository;
import java.util.List;
import lombok.Data;

@Data
public class DummyRepository<E> implements Repository<E> {

    private String lastMethodCalled;
    private Class configuredClass;
    public static DummyRepository<?> instance;

    public DummyRepository() {
        super();
        instance = this;
    }

    @Override
    public E save(E obj) {
        lastMethodCalled = "save";
        return null;
    }

    @Override
    public void delete(Object id) {
        lastMethodCalled = "delete";

    }

    @Override
    public List<E> list() {
        lastMethodCalled = "list";
        return null;
    }

    @Override
    public E getById(Object id) {
        lastMethodCalled = "getById";
        return null;
    }

    @Override
    public List<E> queryByExample(E obj) {
        lastMethodCalled = "queryByExample";
        return null;
    }
}
