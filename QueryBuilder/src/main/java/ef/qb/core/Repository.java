package ef.qb.core;

import java.util.List;

public interface Repository<E> extends NeedClassConfiguration<E> {

    E save(E obj);

    void delete(E obj);

    List<E> list();

    E getById(Object id);

    List<E> queryByExample(E obj);

}
