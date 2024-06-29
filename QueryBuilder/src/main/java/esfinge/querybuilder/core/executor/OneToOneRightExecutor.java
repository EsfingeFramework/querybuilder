package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryType;
import static esfinge.querybuilder.core.utils.QueryUtils.createSimpleQueryInfo;
import static esfinge.querybuilder.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import java.lang.reflect.Field;
import java.util.List;

public class OneToOneRightExecutor extends AbstractRelationExecutor {

    public OneToOneRightExecutor(QueryExecutor secExecutor) {
        super(secExecutor);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(PolyglotOneToOne.class)
                && field.getDeclaringClass().equals(field.getAnnotation(PolyglotOneToOne.class).referencedEntity());
    }

    @Override
    public Object correlate(Field field, QueryInfo priInfo, Object priResult) {
        var joinAnn = validateAndGetJoinAnnotation(field);
        var joinName = joinAnn.name();
        var joinReferencedAttributeName = joinAnn.referencedAttributeName();
        var secInfo = createSimpleQueryInfo(field, joinName);
        if (priInfo.getQueryType().equals(QueryType.RETRIEVE_LIST)) {
            var priResultList = (List<?>) priResult;
            priResultList.forEach(priItem -> singleCorrelate(field, priInfo, secInfo, priItem, joinReferencedAttributeName));
        } else if (priInfo.getQueryType().equals(QueryType.RETRIEVE_SINGLE)) {
            singleCorrelate(field, priInfo, secInfo, priResult, joinReferencedAttributeName);
        }
        return priResult;
    }
}
