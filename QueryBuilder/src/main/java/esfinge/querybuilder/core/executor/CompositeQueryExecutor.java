package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotOneToMany;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_LIST;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_SINGLE;
import esfinge.querybuilder.core.methodparser.conditions.SimpleCondition;
import java.lang.reflect.Field;
import java.util.List;

public class CompositeQueryExecutor implements QueryExecutor {

    private final QueryExecutor primary;
    private final QueryExecutor secondary;

    public CompositeQueryExecutor(QueryExecutor primary, QueryExecutor secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        var result = primary.executeQuery(info, args);
        var entityType = info.getEntityType();
        var fields = entityType.getDeclaredFields();
        for (var field : fields) {
            if (field.isAnnotationPresent(PolyglotOneToOne.class)) {
                result = processOneToOne(field, info, result);
            } else if (field.isAnnotationPresent(PolyglotOneToMany.class)) {
                result = processOneToMany(field, result);
            }
        }
        return result;
    }

    private Object processOneToOne(Field field, QueryInfo primaryInfo, Object primaryResult) {
        var annotation = field.getAnnotation(PolyglotOneToOne.class);
        var mappedByAttribute = annotation.mappedByAttribute();
        var joinAttribute = annotation.joinAttribute();
        var referencedAttributeKey = annotation.referencedAttributeKey();

        if ((mappedByAttribute.equals("NONE") && joinAttribute.equals("NONE"))
                || (!mappedByAttribute.equals("NONE") && !joinAttribute.equals("NONE"))) {
            throw new RuntimeException("The PolyglotOneToOne annotation must have the mappedByAttribute or joinAttribute attribute defined.");
        }

        var secondaryInfo = createSecondaryQueryInfo(field, mappedByAttribute, referencedAttributeKey);
        return processPrimaryResult(primaryInfo, primaryResult, field, secondaryInfo, mappedByAttribute, joinAttribute, referencedAttributeKey);
    }

    private QueryInfo createSecondaryQueryInfo(Field field, String mappedByAttribute, String referencedAttributeKey) {
        var secondaryInfo = new QueryInfo();
        secondaryInfo.setQueryType(RETRIEVE_LIST);
        secondaryInfo.setEntityType(field.getType());
        secondaryInfo.setEntityName(field.getType().getSimpleName());
        var condition = new SimpleCondition();
        condition.setName(!mappedByAttribute.equals("NONE") ? mappedByAttribute : referencedAttributeKey);
        secondaryInfo.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        secondaryInfo.addCondition(condition);
        return secondaryInfo;
    }

    private Object processPrimaryResult(QueryInfo primaryInfo, Object primaryResult, Field field, QueryInfo secondaryInfo,
            String mappedByAttribute, String joinAttribute, String referencedAttributeKey) {
        var primaryClass = primaryInfo.getEntityType();
        if (primaryInfo.getQueryType().equals(RETRIEVE_LIST)) {
            var primaryResultList = (List<?>) primaryResult;
            for (var priItem : primaryResultList) {
                processSingleResult(priItem, primaryClass, field, secondaryInfo, mappedByAttribute, joinAttribute, referencedAttributeKey);
            }
        } else if (primaryInfo.getQueryType().equals(RETRIEVE_SINGLE)) {
            processSingleResult(primaryResult, primaryClass, field, secondaryInfo, mappedByAttribute, joinAttribute, referencedAttributeKey);
        }
        return primaryResult;
    }

    private void processSingleResult(Object primaryResult, Class<?> primaryClass, Field field, QueryInfo secondaryInfo,
            String mappedByAttribute, String joinAttribute, String referencedAttributeKey) {
        try {
            var castItem = primaryClass.cast(primaryResult);
            var columnToMatch = !joinAttribute.equals("NONE") ? joinAttribute : referencedAttributeKey;
            var primaryField = primaryClass.getDeclaredField(columnToMatch);
            primaryField.setAccessible(true);
            var valuePrimary = primaryField.get(castItem);
            var secondaryResults = (List<?>) secondary.executeQuery(secondaryInfo, new Object[]{valuePrimary});
            for (var secItem : secondaryResults) {
                var secondaryField = secItem.getClass().getDeclaredField(mappedByAttribute.equals("NONE") ? referencedAttributeKey : mappedByAttribute);
                secondaryField.setAccessible(true);
                var valueSecondary = secondaryField.get(secItem);
                if (valuePrimary.equals(valueSecondary)) {
                    field.setAccessible(true);
                    field.set(castItem, secItem);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private Object processOneToMany(Field field, Object result) {
        // Implementar lógica para OneToMany se necessário
        return result;
    }
}
