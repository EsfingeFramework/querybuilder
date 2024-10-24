package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import static esfinge.querybuilder.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;

public class OneToOneLeftProcessor extends AbstractRelationProcessor {

    public OneToOneLeftProcessor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getType().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }

    @Override
    public void configure(Field field) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        setPriAttribute(joinAnn.name());
        setSecAttribute(joinAnn.referencedAttributeName());
    }
}
