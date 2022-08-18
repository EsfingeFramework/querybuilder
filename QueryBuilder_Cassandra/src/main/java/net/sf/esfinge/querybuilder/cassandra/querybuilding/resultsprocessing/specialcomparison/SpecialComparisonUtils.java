package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                return true;
        }
    }

    public static boolean filterBySpecialComparisonClause(Object parameterValue, SpecialComparisonClause clause) {
        return filterBySpecialComparison(parameterValue, clause.getValue(), clause.getSpecialComparisonType());
    }

    public static <E> List filterListBySpecialComparisonClause(List<E> list, SpecialComparisonClause clause) {
        if (list.size() == 0)
            return list;

        Class clazz = list.get(0).getClass();

        Method[] getters = CassandraReflectionUtils.getClassGetters(clazz);

        Method getter = CassandraReflectionUtils.getClassGetterForField(clazz, getters, clause.getPropertyName());

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
        }).collect(Collectors.toList());
    }

    public static List<SpecialComparisonClause> getSpecialComparisonClausesWithValues(Object[] args, List<SpecialComparisonClause> spc) {
        List<SpecialComparisonClause> newSpc = new ArrayList<>();

        for (SpecialComparisonClause c : spc) {
            if (c.getValue() == null)
                c.setValue(args[c.getArgPosition()]);

            newSpc.add(c);
        }

        return newSpc;
    }

    public static boolean hasCompareToNullAnnotationOnFields(Object obj) {
        if (obj != null) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(CompareToNull.class))
                    return true;
            }
        }

        return false;
    }
}
