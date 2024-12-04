package ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;

import ef.qb.cassandra.exceptions.ComparisonTypeNotFoundException;
import ef.qb.cassandra.exceptions.MethodInvocationException;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetterForField;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import ef.qb.core.annotation.CompareToNull;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class SpecialComparisonUtils {

    public static boolean filterBySpecialComparison(Object objectAttributeValue, Object queryParameterValue, SpecialComparisonType comparisonType) {
        switch (comparisonType) {
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
                throw new ComparisonTypeNotFoundException("");
        }
    }

    public static boolean filterBySpecialComparisonClause(Object parameterValue, SpecialComparisonClause clause) {
        return filterBySpecialComparison(parameterValue, clause.getValue(), clause.getSpecialComparisonType());
    }

    public static <E> List filterListBySpecialComparisonClause(List<E> list, SpecialComparisonClause clause) {
        if (list.size() == 0) {
            return list;
        }

        Class clazz = list.get(0).getClass();

        var getters = getClassGetters(clazz);
        var getter = getClassGetterForField(clazz, getters, clause.getPropertyName());

        return list.stream().filter(obj -> {
            try {
                // If we have the @CompareToNull annotation on the parameter of the query method
                // but we pass a non null value to the method, then we should skip attributes
                // in the results which are not null
                if (getter.invoke(obj) == null && clause.getValue() != null) {
                    return false;
                } else {
                    return filterBySpecialComparisonClause(getter.invoke(obj), clause);
                }
            } catch (Exception e) {
                throw new MethodInvocationException("Could not invoke method \"" + getter.getName() + "\" on object \"" + obj + "\", this is caused by: " + e);
            }
        }).collect(toList());
    }

    public static List<SpecialComparisonClause> getSpecialComparisonClausesWithValues(Object[] args, List<SpecialComparisonClause> spc) {
        List<SpecialComparisonClause> newSpc = new ArrayList<>();

        for (var c : spc) {
            if (c.getValue() == null) {
                c.setValue(args[c.getArgPosition()]);
            }

            newSpc.add(c);
        }

        return newSpc;
    }

    public static boolean hasCompareToNullAnnotationOnFields(Object obj) {
        if (obj != null) {
            for (var f : obj.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(CompareToNull.class)) {
                    return true;
                }
            }
        }

        return false;
    }
}
