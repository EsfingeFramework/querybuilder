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

    private QueryExecutor primary;
    private QueryExecutor secondary;

    public CompositeQueryExecutor(QueryExecutor primary, QueryExecutor secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        Object result;
        result = primary.executeQuery(info, args);
        Class entityType = info.getEntityType();
        Field[] fields = entityType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PolyglotOneToOne.class)) {
                result = invokeOneToOne(field, info, result);
            } else if (field.isAnnotationPresent(PolyglotOneToMany.class)) {
                result = invokeOneToMany(field, result);
            }
        }
        return result;
    }

    private Object invokeOneToOne(Field field, QueryInfo primaryInfo, Object primaryResult) {
        var mappedBy = field.getAnnotation(PolyglotOneToOne.class).mappedBy();
        var joinColumn = field.getAnnotation(PolyglotOneToOne.class).joinColumn();
        var referencedColumnName = field.getAnnotation(PolyglotOneToOne.class).referencedColumnName();

        if ((mappedBy.equals("NONE") && joinColumn.equals("NONE"))
                || (!mappedBy.equals("NONE") && !joinColumn.equals("NONE"))) {
            throw new RuntimeException("The PolyglotOneToOne annotation must have the mappedBy or joinColumn attribute defined.");
        } else if (!mappedBy.equals("NONE") && joinColumn.equals("NONE")) {
            var secondaryInfo = new QueryInfo();
            secondaryInfo.setQueryType(RETRIEVE_LIST);
            secondaryInfo.setEntityType(field.getType());
            secondaryInfo.setEntityName(field.getType().getSimpleName());
            var qc = new SimpleCondition();
            qc.setName(mappedBy);
            secondaryInfo.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
            secondaryInfo.addCondition(qc);

            var primaryClass = primaryInfo.getEntityType();
            if (primaryInfo.getQueryType().equals(RETRIEVE_LIST)) {
                var primaryResultList = (List<?>) primaryResult;

                for (var priItem : primaryResultList) {
                    try {
                        var castItem = primaryClass.cast(priItem);

                        // Obtem o valor do campo referenciado de castItem
                        Field fieldPrimary = primaryClass.getDeclaredField(referencedColumnName);
                        fieldPrimary.setAccessible(true);
                        var valuePrimary = fieldPrimary.get(castItem);

                        // Executa a query secundária
                        var secondaryResult = (List<?>) secondary.executeQuery(secondaryInfo, new Object[]{valuePrimary});
                        for (var secItem : secondaryResult) {
                            // Obtem o valor do campo mappedBy de secItem
                            Field mappedByFieldSecondary = secItem.getClass().getDeclaredField(mappedBy);
                            mappedByFieldSecondary.setAccessible(true);
                            var mappedByValueSecondary = mappedByFieldSecondary.get(secItem);

                            // Compara os valores
                            if (valuePrimary.equals(mappedByValueSecondary)) {
                                field.setAccessible(true);
                                field.set(castItem, secItem);
                            }
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (primaryInfo.getQueryType().equals(RETRIEVE_SINGLE)) {
                try {
                    var castItem = primaryClass.cast(primaryResult);

                    // Obtem o valor do campo referenciado de castItem
                    Field fieldPrimary = primaryClass.getDeclaredField(referencedColumnName);
                    fieldPrimary.setAccessible(true);
                    var valuePrimary = fieldPrimary.get(castItem);

                    // Executa a query secundária
                    var secondaryResult = (List<?>) secondary.executeQuery(secondaryInfo, new Object[]{valuePrimary});
                    for (var secItem : secondaryResult) {
                        // Obtem o valor do campo mappedBy de secItem
                        Field mappedByFieldSecondary = secItem.getClass().getDeclaredField(mappedBy);
                        mappedByFieldSecondary.setAccessible(true);
                        var mappedByValueSecondary = mappedByFieldSecondary.get(secItem);

                        // Compara os valores
                        if (valuePrimary.equals(mappedByValueSecondary)) {
                            field.setAccessible(true);
                            field.set(castItem, secItem);
                        }
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (mappedBy.equals("NONE") && !joinColumn.equals("NONE")) {
            var secondaryInfo = new QueryInfo();
            secondaryInfo.setQueryType(RETRIEVE_LIST);
            secondaryInfo.setEntityType(field.getType());
            secondaryInfo.setEntityName(field.getType().getSimpleName());
            var qc = new SimpleCondition();
            qc.setName(referencedColumnName);
            secondaryInfo.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
            secondaryInfo.addCondition(qc);

            var primaryClass = primaryInfo.getEntityType();
            if (primaryInfo.getQueryType().equals(RETRIEVE_LIST)) {
                var primaryResultList = (List<?>) primaryResult;

                for (var priItem : primaryResultList) {
                    try {
                        var castItem = primaryClass.cast(priItem);

                        // Obtem o valor do campo joinColumn de castItem
                        Field fieldPrimary = primaryClass.getDeclaredField(joinColumn);
                        fieldPrimary.setAccessible(true);
                        var valuePrimary = fieldPrimary.get(castItem);

                        // Executa a query secundária
                        var secondaryResult = (List<?>) secondary.executeQuery(secondaryInfo, new Object[]{valuePrimary});
                        for (var secItem : secondaryResult) {
                            // Obtem o valor do campo mappedBy de secItem
                            Field mappedByFieldSecondary = secItem.getClass().getDeclaredField(referencedColumnName);
                            mappedByFieldSecondary.setAccessible(true);
                            var mappedByValueSecondary = mappedByFieldSecondary.get(secItem);

                            // Compara os valores
                            if (valuePrimary.equals(mappedByValueSecondary)) {
                                field.setAccessible(true);
                                field.set(castItem, secItem);
                            }
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (primaryInfo.getQueryType().equals(RETRIEVE_SINGLE)) {
                try {
                    var castItem = primaryClass.cast(primaryResult);

                    // Obtem o valor do campo joinColumn de castItem
                    Field fieldPrimary = primaryClass.getDeclaredField(joinColumn);
                    fieldPrimary.setAccessible(true);
                    var valuePrimary = fieldPrimary.get(castItem);

                    // Executa a query secundária
                    var secondaryResult = (List<?>) secondary.executeQuery(secondaryInfo, new Object[]{valuePrimary});
                    for (var secItem : secondaryResult) {
                        // Obtem o valor do campo mappedBy de secItem
                        Field mappedByFieldSecondary = secItem.getClass().getDeclaredField(mappedBy);
                        mappedByFieldSecondary.setAccessible(true);
                        var mappedByValueSecondary = mappedByFieldSecondary.get(secItem);

                        // Compara os valores
                        if (valuePrimary.equals(mappedByValueSecondary)) {
                            field.setAccessible(true);
                            field.set(castItem, secItem);
                        }
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return primaryResult;
    }

    private Object invokeOneToMany(Field field, Object result) {
        return null;
    }

}
