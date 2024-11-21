package ef.qb.cassandra.querybuilding.resultsprocessing.join;

import ef.qb.cassandra.exceptions.MethodInvocationException;
import ef.qb.cassandra.exceptions.UnsupportedComparisonException;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetterForField;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class JoinUtils {

    public static boolean filterByJoinClauseComparisonType(Object objectAttributeValue, Object queryParameterValue, JoinComparisonType comparisonType) {
        switch (comparisonType) {
            case EQUALS:
                return objectAttributeValue.equals(queryParameterValue);
            case GREATER_OR_EQUALS:
                return objectAttributeValue.equals(queryParameterValue) || (Double) objectAttributeValue >= (Double) queryParameterValue;
            case LESSER_OR_EQUALS:
                return objectAttributeValue.equals(queryParameterValue) || (Double) objectAttributeValue <= (Double) queryParameterValue;
            case GREATER:
                return (Double) objectAttributeValue > (Double) queryParameterValue;
            case LESSER:
                return (Double) objectAttributeValue < (Double) queryParameterValue;
            case NOT_EQUALS:
                return !objectAttributeValue.equals(queryParameterValue);
            case STARTS:
                return objectAttributeValue.toString().startsWith(queryParameterValue.toString());
            case ENDS:
                return objectAttributeValue.toString().endsWith(queryParameterValue.toString());
            case CONTAINS:
                return objectAttributeValue.toString().contains(queryParameterValue.toString());
            case COMPARE_TO_NULL:
                return queryParameterValue == null ? objectAttributeValue == null : objectAttributeValue.equals(queryParameterValue);
            default:
                return true;
        }
    }

    public static boolean filterByJoinClauseComparisonType(Double objectAttributeValue, Double queryParameterValue, JoinComparisonType comparisonType) {
        switch (comparisonType) {
            case EQUALS:
                return objectAttributeValue.equals(queryParameterValue);
            case GREATER_OR_EQUALS:
                return objectAttributeValue >= queryParameterValue;
            case LESSER_OR_EQUALS:
                return objectAttributeValue <= queryParameterValue;
            case GREATER:
                return objectAttributeValue > queryParameterValue;
            case LESSER:
                return objectAttributeValue < queryParameterValue;
            case NOT_EQUALS:
                return !objectAttributeValue.equals(queryParameterValue);
            case STARTS:
            case ENDS:
            case CONTAINS:
                throw new UnsupportedComparisonException(comparisonType + " not supported for " + objectAttributeValue + " and " + queryParameterValue);
            case COMPARE_TO_NULL:
                return queryParameterValue == null ? objectAttributeValue == null : objectAttributeValue.equals(queryParameterValue);
            default:
                return true;
        }
    }

    public static boolean filterByJoinClauseComparisonType(Integer objectAttributeValue, Integer queryParameterValue, JoinComparisonType comparisonType) {
        switch (comparisonType) {
            case EQUALS:
                return objectAttributeValue.equals(queryParameterValue);
            case GREATER_OR_EQUALS:
                return objectAttributeValue >= queryParameterValue;
            case LESSER_OR_EQUALS:
                return objectAttributeValue <= queryParameterValue;
            case GREATER:
                return objectAttributeValue > queryParameterValue;
            case LESSER:
                return objectAttributeValue < queryParameterValue;
            case NOT_EQUALS:
                return !objectAttributeValue.equals(queryParameterValue);
            case STARTS:
            case ENDS:
            case CONTAINS:
                throw new UnsupportedComparisonException(comparisonType + " not supported for " + objectAttributeValue + " and " + queryParameterValue);
            case COMPARE_TO_NULL:
                return queryParameterValue == null ? objectAttributeValue == null : objectAttributeValue.equals(queryParameterValue);
            default:
                return true;
        }
    }

    public static boolean filterByJoinClause(Object parameterValue, JoinClause joinClause) {
        return filterByJoinClauseComparisonType(parameterValue, joinClause.getValue(), joinClause.getComparisonType());
    }

    public static <E> List filterListByJoinClause(List<E> list, JoinClause joinClause) {
        if (list.size() == 0)
            return list;

        Class<?> mainClass = list.get(0).getClass();
        Method[] mainGetters = getClassGetters(mainClass);
        Method mainGetter = getClassGetterForField(mainClass, mainGetters, joinClause.getJoinTypeName());

        return list.stream().filter(obj -> {
            Method joinGetter = null;

            try {
                Class joinClass = mainGetter.invoke(list.get(0)).getClass();

                Method[] joinGetters = getClassGetters(joinClass);
                joinGetter = getClassGetterForField(joinClass, joinGetters, joinClause.getJoinAttributeName());

                Object nestedClass = mainGetter.invoke(obj);

                // If we have the @CompareToNull annotation on the parameter of the query method
                // but we pass a non null value to the method, then we should skip attributes
                // in the results which are not null
                if (joinGetter.invoke(nestedClass) == null && joinClause.getValue() != null) {
                    return false;
                } else {
                    return filterByJoinClause(joinGetter.invoke(nestedClass), joinClause);
                }
            } catch (Exception e) {
                assert joinGetter != null;
                throw new MethodInvocationException("Could not invoke method \"" + joinGetter.getName() + "\" on object \"" + obj + "\", this is caused by: " + e);
            }
        }).collect(toList());
    }

    public static List<JoinClause> getJoinClausesWithValues(Object[] args, List<JoinClause> jcs) {
        List<JoinClause> newJcs = new ArrayList<>();

        if (args == null)
            return jcs;

        for (JoinClause j : jcs) {
            if (j.getValue() == null)
                j.setValue(args[j.getArgPosition()]);

            newJcs.add(j);
        }

        return newJcs;
    }


}
