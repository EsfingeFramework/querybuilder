package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotOneToMany;
import java.lang.reflect.Field;

public class OneToManyExecutor extends AbstractRelationExecutor {

    public OneToManyExecutor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToMany.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToMany.class).referencedEntity());
    }
}
