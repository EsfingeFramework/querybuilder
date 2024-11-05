package ef.qb.core.executor;

import ef.qb.core.annotation.PolyglotOneToMany;
import java.lang.reflect.Field;

public class OneToManyProcessor extends AbstractRelationProcessor {

    public OneToManyProcessor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToMany.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToMany.class).referencedEntity());
    }
}
