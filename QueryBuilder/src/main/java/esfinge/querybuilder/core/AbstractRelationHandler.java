package esfinge.querybuilder.core;

import java.lang.reflect.Field;
import java.util.Collection;

public abstract class AbstractRelationHandler<E> implements RelationHandler<E> {

    @Override
    public void handleDelete(Field field, E obj, Repository secRepository) {
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
