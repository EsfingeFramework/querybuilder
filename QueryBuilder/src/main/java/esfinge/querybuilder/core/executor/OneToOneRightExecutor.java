package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import java.lang.reflect.Field;

public class OneToOneRightExecutor extends AbstractRelationExecutor {

    public OneToOneRightExecutor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }
}
