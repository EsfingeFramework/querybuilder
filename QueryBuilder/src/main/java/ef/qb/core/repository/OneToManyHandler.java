package ef.qb.core.repository;

import ef.qb.core.Repository;
import ef.qb.core.annotation.PolyglotOneToMany;
import static ef.qb.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class OneToManyHandler<E> implements RelationHandler<E> {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToMany.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToMany.class).referencedEntity());
    }

    @Override
    public E handleSave(Field field, E obj, Repository<E> priRepository, Repository secRepository) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();
        try {
            var mergeObj = priRepository.save(obj);
            field.setAccessible(true);
            var collection = (Collection<?>) field.get(obj);
            if (collection != null) {
                Collection<Object> savedItems = collection.getClass().getDeclaredConstructor().newInstance();
                for (var item : collection) {
                    if (item != null) {
                        secRepository.configureClass(item.getClass());
                        if (!joinName.contains(".")) {
                            var joinField = item.getClass().getDeclaredField(joinName);
                            joinField.setAccessible(true);
                            var priKey = mergeObj.getClass().getDeclaredField(referencedAttributeName);
                            priKey.setAccessible(true);
                            var priKeyValue = priKey.get(mergeObj);
                            joinField.set(item, priKeyValue);
                        }
                        savedItems.add(secRepository.save(item));
                    }
                }
                if (!savedItems.isEmpty()) {
                    field.set(mergeObj, savedItems);
                }
            }
            return mergeObj;
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException | NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
