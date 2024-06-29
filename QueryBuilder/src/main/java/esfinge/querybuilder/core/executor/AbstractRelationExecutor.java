package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryType;
import static esfinge.querybuilder.core.utils.QueryUtils.createSimpleQueryInfo;
import static esfinge.querybuilder.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRelationExecutor implements RelationExecutor {

    protected final QueryExecutor secExecutor;
    private String priAttribute;
    private String secAttribute;

    protected AbstractRelationExecutor(QueryExecutor secExecutor) {
        this.secExecutor = secExecutor;
    }

    @Override
    public void configure(Field field) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        setPriAttribute(joinAnn.referencedAttributeName());
        setSecAttribute(joinAnn.name());
    }

    @Override
    public Object correlate(Field field, QueryInfo priInfo, Object priResult) {
        configure(field);
        var secInfo = createSimpleQueryInfo(field, getSecAttribute());
        if (priInfo.getQueryType().equals(QueryType.RETRIEVE_LIST)) {
            var priResultList = (List<?>) priResult;
            priResultList.forEach(priItem -> singleCorrelate(field, priInfo, secInfo, priItem, getPriAttribute()));
        } else if (priInfo.getQueryType().equals(QueryType.RETRIEVE_SINGLE)) {
            singleCorrelate(field, priInfo, secInfo, priResult, getPriAttribute());
        }
        return priResult;
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
