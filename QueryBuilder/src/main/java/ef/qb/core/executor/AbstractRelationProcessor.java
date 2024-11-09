package ef.qb.core.executor;

import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryType;
import static ef.qb.core.utils.QueryUtils.createSimpleQueryInfo;
import static ef.qb.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public abstract class AbstractRelationProcessor implements RelationProcessor {

    protected final QueryExecutor secExecutor;
    private String priAttribute;
    private String secAttribute;

    protected AbstractRelationProcessor(QueryExecutor secExecutor) {
        this.secExecutor = secExecutor;
    }

    public String getPriAttribute() {
        return priAttribute;
    }

    public void setPriAttribute(String priAttribute) {
        this.priAttribute = priAttribute;
    }

    public String getSecAttribute() {
        return secAttribute;
    }

    public void setSecAttribute(String secAttribute) {
        this.secAttribute = secAttribute;
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
