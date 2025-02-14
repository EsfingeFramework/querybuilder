package ef.qb.core.executor;

import ef.qb.core.annotation.PolyglotOneToOne;
import java.lang.reflect.Field;

public class OneToOneRightProcessor extends AbstractRelationProcessor {

    public OneToOneRightProcessor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }
}
