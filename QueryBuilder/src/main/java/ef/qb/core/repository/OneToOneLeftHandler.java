package ef.qb.core.repository;

import ef.qb.core.Repository;
import ef.qb.core.annotation.PolyglotOneToOne;
import static ef.qb.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;

public class OneToOneLeftHandler<E> implements RelationHandler<E> {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getType().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }

    @Override
    public E handleSave(Field field, E obj, Repository<E> priRepository, Repository secRepository) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();
        try {
            field.setAccessible(true);
            var fieldValue = field.get(obj);
            if (fieldValue != null) {
                secRepository.configureClass(field.getType());
                var secSaved = secRepository.save(fieldValue);
                if (secSaved != null) {
                    field.set(obj, secSaved);
                    var castSec = field.getType().cast(secSaved);
                    var joinField = obj.getClass().getDeclaredField(joinName);
                    joinField.setAccessible(true);
                    var secKey = castSec.getClass().getDeclaredField(referencedAttributeName);
                    secKey.setAccessible(true);
                    var secKeyValue = secKey.get(castSec);
                    joinField.set(obj, secKeyValue);
                    return priRepository.save(obj);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
