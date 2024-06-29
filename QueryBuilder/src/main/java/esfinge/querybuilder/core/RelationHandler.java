package esfinge.querybuilder.core;

import java.lang.reflect.Field;

public interface RelationHandler<E> {

    boolean supports(Field field);

    E handleSave(Field field, E obj, Repository<E> priRepository, Repository secRepository);

    void handleDelete(Field field, E entity, Repository secRepository);
}
