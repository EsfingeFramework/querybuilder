package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;

public class QueryBuildingUtils {

    public static String replaceQueryArgs(String query, Object[] args) {
        int paramOccurrence = countOccurrenceOfParametersInQuery(query);

        if (paramOccurrence != args.length)
            throw new QueryParametersMismatchException("Number of parameters in the query different from the number of arguments");

        String newQuery = query;

        int current = 0;

        for (int i = 0; i < paramOccurrence; i++) {
            newQuery = newQuery.substring(0, newQuery.indexOf('?')) + getValueRepresentationByType(args[current]) + newQuery.substring(newQuery.indexOf('?') + 1);
            current++;
        }

        return newQuery;
    }

    private static int countOccurrenceOfParametersInQuery(String string) {
        return string.chars()
                .filter(c -> c == '?')
                .toArray()
                .length;
    }

    public static String getValueRepresentationByType(Object value) {
        String className = value.getClass().getSimpleName();

        if (className.equals("String") || className.equals("Character"))
            return "'" + value + "'";

        return "" + value + "";
    }
}
