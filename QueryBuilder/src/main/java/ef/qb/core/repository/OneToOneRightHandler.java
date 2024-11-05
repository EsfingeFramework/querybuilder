package ef.qb.core.repository;

import ef.qb.core.Repository;
import ef.qb.core.annotation.PolyglotOneToOne;
import static ef.qb.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;

public class OneToOneRightHandler<E> implements RelationHandler<E> {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }

    @Override
    public E handleSave(Field field, E obj, Repository<E> priRepository, Repository secRepository) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();
        try {
            obj = priRepository.save(obj);
            field.setAccessible(true);
            var fieldValue = field.get(obj);
            if (fieldValue != null) {
                var priKey = obj.getClass().getDeclaredField(referencedAttributeName);
                priKey.setAccessible(true);
                var priKeyValue = priKey.get(obj);
                var joinField = fieldValue.getClass().getDeclaredField(joinName);
                joinField.setAccessible(true);
                joinField.set(fieldValue, priKeyValue);
                secRepository.configureClass(field.getType());
                var secSaved = secRepository.save(fieldValue);
                if (secSaved != null) {
                    field.set(obj, secSaved);
                    return obj;
                }
            }

        } catch (IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
