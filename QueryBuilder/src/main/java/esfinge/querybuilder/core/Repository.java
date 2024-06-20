package esfinge.querybuilder.core;

import java.util.List;

public interface Repository<E> extends NeedClassConfiguration<E> {

    E save(E obj);

    void delete(Object id);

    List<E> list();

    E getById(Object id);

    List<E> queryByExample(E obj);

}
