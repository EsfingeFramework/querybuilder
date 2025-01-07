package ef.qb.core.executor;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotOneToOne;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.conditions.CompositeCondition;
import ef.qb.core.methodparser.conditions.QueryCondition;
import ef.qb.core.methodparser.conditions.SimpleCondition;
import ef.qb.core.utils.QueryUtils;
import static ef.qb.core.utils.QueryUtils.validateAndGetJoinAnnotation;
import ef.qb.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CompositeQueryExecutor implements QueryExecutor {

    private final QueryExecutor priExecutor;
    private final QueryExecutor secExecutor;
    private final List<RelationProcessor> relationProcessors;

    public CompositeQueryExecutor(QueryExecutor priExecutor, QueryExecutor secExecutor) {
        this.priExecutor = Objects.requireNonNull(priExecutor, "Primary QueryExecutor cannot be null");
        this.secExecutor = Objects.requireNonNull(secExecutor, "Secondary QueryExecutor cannot be null");
        this.relationProcessors = List.of(
                new OneToOneLeftProcessor(this.secExecutor),
                new OneToOneRightProcessor(this.secExecutor),
                new OneToManyProcessor(this.secExecutor)
        );
    }

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        Objects.requireNonNull(info, "QueryInfo cannot be null");
        var compQueryObjList = processDifferentPersistences(info, args);
        var result = new ArrayList<Object>();
        for (var compQueryObj : compQueryObjList) {
            var priResult = priExecutor.executeQuery(compQueryObj.info, compQueryObj.args);
            if (priResult == null) {
                continue;
            }
            var entityType = info.getEntityType();
            for (var field : entityType.getDeclaredFields()) {
                for (var processor : relationProcessors) {
                    if (processor.supports(field)) {
                        priResult = processor.correlate(field, info, priResult);
                    }
                }
            }
            result.addAll((Collection) priResult);
        }
        return result;
    }

    private List<CompositeQueryObject> processDifferentPersistences(final QueryInfo info, final Object[] args) {
        var result = new ArrayList<CompositeQueryObject>();
        var parameterNames = info.getCondition().getParameterNames();
        var index = 0;
        for (var parameterName : parameterNames) {
            if (parameterName.contains(".")) {
                try {
                    var words = parameterName.split("\\.");
                    var secFieldName = words[0];
                    var secAttribute = words[1];
                    Class<?> entityClass = info.getEntityType();
                    var returnType = entityClass.getMethod("get" + StringUtils.firstCharWithUppercase(secFieldName)).getReturnType();
                    if (returnType.isAnnotationPresent(PersistenceType.class)) {
                        var principal = entityClass.getAnnotation(PersistenceType.class).value();
                        var child = returnType.getAnnotation(PersistenceType.class).value();
                        if (!principal.equals(child)) {
                            // different persistences
                            var secField = entityClass.getDeclaredField(secFieldName);
                            secField.setAccessible(true);
                            var secInfo = QueryUtils.createSimpleQueryInfo(secField, secAttribute);
                            var secArgs = new Object[]{args[index]};
                            var secResult = (Collection<?>) secExecutor.executeQuery(secInfo, secArgs);
                            var iterator = secResult.iterator();
                            while (iterator.hasNext()) {
                                var secItem = iterator.next();
                                String newSecFieldName;
                                String priFieldName;
                                Object newArg = null;
                                var joinAnn = validateAndGetJoinAnnotation(secField);
                                if (secField.isAnnotationPresent(PolyglotOneToOne.class)) {
                                    if (secField.getType().equals(secField.getAnnotation(PolyglotOneToOne.class).referencedEntity())) {
                                        newSecFieldName = joinAnn.referencedAttributeName();
                                        priFieldName = joinAnn.name();
                                    } else {
                                        newSecFieldName = joinAnn.name();
                                        priFieldName = joinAnn.referencedAttributeName();
                                    }
                                    if (secItem != null) {
                                        var secObj = secField.getType().cast(secItem);
                                        var field = secObj.getClass().getDeclaredField(newSecFieldName);
                                        field.setAccessible(true);
                                        newArg = field.get(secObj);
                                    }
                                    var newInfo = updateQueryInfo(info, index, priFieldName);
                                    @SuppressWarnings("MismatchedReadAndWriteOfArray")
                                    var newArgs = args.clone();
                                    newArgs[index] = newArg;
                                    result.add(new CompositeQueryObject(newInfo, newArgs));
                                } else {
                                    throw new UnsupportedOperationException("Limitation: Only implemented for @PolyglotOneToOne.");
                                }
                            }
                        }
                    } else {
                        throw new UnsupportedOperationException("@PersistenceType not found.");
                    }
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
                    ex.printStackTrace();
                }
            }
            index++;
        }
        if (result.isEmpty()) {
            result.add(new CompositeQueryObject(info, args));
        }
        return result;
    }

    private QueryInfo updateQueryInfo(QueryInfo info, Integer index, String newFieldName) {
        var condition = findConditionByIndex(info.getCondition(), index, new int[]{0});
        if (condition instanceof SimpleCondition) {
            ((SimpleCondition) condition).setName(newFieldName);
        }
        return info;
    }

    private QueryCondition findConditionByIndex(QueryCondition condition, int targetIndex, int[] currentIndex) {
        if (condition instanceof SimpleCondition) {
            if (currentIndex[0] == targetIndex) {
                return condition;
            }
            currentIndex[0]++;
        } else if (condition instanceof CompositeCondition) {
            var composite = (CompositeCondition) condition;
            var found = findConditionByIndex(composite.getFirstCondition(), targetIndex, currentIndex);
            if (found != null) {
                return found;
            }
            found = findConditionByIndex(composite.getSecondCondition(), targetIndex, currentIndex);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    class CompositeQueryObject {

        protected QueryInfo info;
        protected Object[] args;

        public CompositeQueryObject(QueryInfo info, Object[] args) {
            this.info = info;
            this.args = args;
        }
    }
}
