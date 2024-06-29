package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.methodparser.QueryInfo;
import java.lang.reflect.Field;
import java.util.Collection;

public abstract class AbstractRelationExecutor implements RelationExecutor {

    protected final QueryExecutor secExecutor;

    protected AbstractRelationExecutor(QueryExecutor secExecutor) {
        this.secExecutor = secExecutor;
    }

    protected void singleCorrelate(Field field, QueryInfo priInfo, QueryInfo secInfo, Object priResult, String priAttribute) {
        try {
            var priClass = priInfo.getEntityType();
            var priItem = priClass.cast(priResult);
            var priField = priClass.getDeclaredField(priAttribute);
            priField.setAccessible(true);
            var priValue = priField.get(priItem);
            var secResult = (Collection<?>) secExecutor.executeQuery(secInfo, new Object[]{priValue});
            field.setAccessible(true);
            if (Collection.class.isAssignableFrom(field.getType())) {
                field.set(priItem, secResult);
            } else {
                var secItem = (secResult != null && secResult.iterator().hasNext()) ? secResult.iterator().next() : null;
                field.set(priItem, secItem);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

}
