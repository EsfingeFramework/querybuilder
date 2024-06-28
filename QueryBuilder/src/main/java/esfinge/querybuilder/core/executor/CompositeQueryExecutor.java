package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.annotation.PolyglotJoin;
import esfinge.querybuilder.core.annotation.PolyglotOneToMany;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_LIST;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_SINGLE;
import esfinge.querybuilder.core.methodparser.conditions.SimpleCondition;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CompositeQueryExecutor implements QueryExecutor {

    private final QueryExecutor primaryExec;
    private final QueryExecutor secondaryExec;

    public CompositeQueryExecutor(QueryExecutor primaryExec, QueryExecutor secondaryExec) {
        this.primaryExec = Objects.requireNonNull(primaryExec, "Primary executor cannot be null");
        this.secondaryExec = Objects.requireNonNull(secondaryExec, "Secondary executor cannot be null");
    }

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        Objects.requireNonNull(info, "QueryInfo cannot be null");
        var result = primaryExec.executeQuery(info, args);
        if (result == null) {
            return null;
        }
        var entityType = info.getEntityType();
        for (var field : entityType.getDeclaredFields()) {
            if (field.isAnnotationPresent(PolyglotOneToOne.class)) {
                result = processOneToOne(field, info, result);
            } else if (field.isAnnotationPresent(PolyglotOneToMany.class)) {
                result = processOneToMany(field, info, result);
            }
        }
        return result;
    }

    private Object processOneToOne(Field field, QueryInfo primaryInfo, Object primaryResult) {
        var oneToOneAnn = field.getAnnotation(PolyglotOneToOne.class);
        var joinAnn = Objects.requireNonNull(field.getAnnotation(PolyglotJoin.class), "PolyglotJoin is required on field annotated with PolyglotOneToOne.");

        var referencedEntity = oneToOneAnn.referencedEntity();
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();

        var secondaryInfo = createSecondaryQueryInfo(field, referencedEntity, joinName, referencedAttributeName);
        return processPrimaryResult(primaryInfo, primaryResult, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
    }

    private QueryInfo createSecondaryQueryInfo(Field field, Class<?> referencedEntity, String joinName, String referencedAttributeName) {
        var fieldType = field.getType();
        if (Collection.class.isAssignableFrom(fieldType)) {
            var genericType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            fieldType = genericType;
        }
        var secondaryInfo = new QueryInfo();
        secondaryInfo.setQueryType(RETRIEVE_LIST);
        secondaryInfo.setEntityType(fieldType);
        secondaryInfo.setEntityName(fieldType.getSimpleName());
        var condition = new SimpleCondition();
        condition.setName(fieldType.equals(referencedEntity) ? referencedAttributeName : joinName);
        secondaryInfo.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        secondaryInfo.addCondition(condition);
        return secondaryInfo;
    }

    private Object processPrimaryResult(QueryInfo primaryInfo, Object primaryResult, Field field, QueryInfo secondaryInfo,
            Class<?> referencedEntity, String joinName, String referencedAttributeName) {
        var primaryClass = primaryInfo.getEntityType();
        if (primaryInfo.getQueryType().equals(RETRIEVE_LIST)) {
            var primaryResultList = (List<?>) primaryResult;
            for (var priItem : primaryResultList) {
                processSingleResult(priItem, primaryClass, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
            }
        } else if (primaryInfo.getQueryType().equals(RETRIEVE_SINGLE)) {
            processSingleResult(primaryResult, primaryClass, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
        }
        return primaryResult;
    }

    private void processSingleResult(Object primaryResult, Class<?> primaryClass, Field field, QueryInfo secondaryInfo,
            Class<?> referencedEntity, String joinName, String referencedAttributeName) {
        try {
            var fieldType = field.getType();
            var castItem = primaryClass.cast(primaryResult);
            var columnToMatch = fieldType.equals(referencedEntity) ? joinName : referencedAttributeName;
            var primaryField = primaryClass.getDeclaredField(columnToMatch);
            primaryField.setAccessible(true);
            var valuePrimary = primaryField.get(castItem);
            var secondaryResults = (List<?>) secondaryExec.executeQuery(secondaryInfo, new Object[]{valuePrimary});
            for (var secItem : secondaryResults) {
                var secondaryField = secItem.getClass().getDeclaredField(fieldType.equals(referencedEntity) ? referencedAttributeName : joinName);
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

    private Object processOneToMany(Field field, QueryInfo primaryInfo, Object primaryResult) {
        var oneToManyAnn = field.getAnnotation(PolyglotOneToMany.class);
        var joinAnn = Objects.requireNonNull(field.getAnnotation(PolyglotJoin.class), "PolyglotJoin is required on field annotated with PolyglotOneToMany.");

        var referencedEntity = oneToManyAnn.referencedEntity();
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();

        var secondaryInfo = createSecondaryQueryInfo(field, referencedEntity, joinName, referencedAttributeName);
        return processPrimaryResultForOneToMany(primaryInfo, primaryResult, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
    }

    private Object processPrimaryResultForOneToMany(QueryInfo primaryInfo, Object primaryResult, Field field, QueryInfo secondaryInfo,
            Class<?> referencedEntity, String joinName, String referencedAttributeName) {
        var primaryClass = primaryInfo.getEntityType();
        if (primaryInfo.getQueryType().equals(RETRIEVE_LIST)) {
            var primaryResultList = (List<?>) primaryResult;
            for (var priItem : primaryResultList) {
                processSingleResultForOneToMany(priItem, primaryClass, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
            }
        } else if (primaryInfo.getQueryType().equals(RETRIEVE_SINGLE)) {
            processSingleResultForOneToMany(primaryResult, primaryClass, field, secondaryInfo, referencedEntity, joinName, referencedAttributeName);
        }
        return primaryResult;
    }

    private void processSingleResultForOneToMany(Object primaryResult, Class<?> primaryClass, Field field, QueryInfo secondaryInfo,
            Class<?> referencedEntity, String joinName, String referencedAttributeName) {
        try {
            var fieldType = field.getType();
            var castItem = primaryClass.cast(primaryResult);
            var columnToMatch = fieldType.equals(referencedEntity) ? joinName : referencedAttributeName;
            var primaryField = primaryClass.getDeclaredField(columnToMatch);
            primaryField.setAccessible(true);
            var valuePrimary = primaryField.get(castItem);
            var secondaryResults = (Collection<?>) secondaryExec.executeQuery(secondaryInfo, new Object[]{valuePrimary});
            field.setAccessible(true);
            field.set(castItem, secondaryResults);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
