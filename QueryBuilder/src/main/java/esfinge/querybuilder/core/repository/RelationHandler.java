package esfinge.querybuilder.core.repository;

import esfinge.querybuilder.core.Repository;
import java.lang.reflect.Field;
import java.util.Collection;

public interface RelationHandler<E> {

    boolean supports(Field field);

    E handleSave(Field field, E obj, Repository<E> priRepository, Repository secRepository);

    default void handleDelete(Field field, E obj, Repository secRepository) {
        try {
            field.setAccessible(true);
            var fieldValue = field.get(obj);
            if (fieldValue != null) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    for (var fieldValueItem : (Collection<?>) fieldValue) {
                        if (fieldValueItem != null) {
                            secRepository.configureClass(fieldValueItem.getClass());
                            secRepository.delete(fieldValueItem);
                        }
                    }
                } else {
                    secRepository.configureClass(field.getType());
                    secRepository.delete(fieldValue);
                }
            }
        } catch (IllegalAccessException | SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
