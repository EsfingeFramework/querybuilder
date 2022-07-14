package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

import java.util.Arrays;
import java.util.Map;

public class QueryBuildingUtils {

    public static String replaceQueryArgs(String query, Object[] args) {
        int paramOccurrence = countOccurrenceOfCharacterInString(query, '?');

        if (paramOccurrence != countNotNullArguments(args))
            throw new QueryParametersMismatchException("Number of parameters in the query different from the number of arguments");

        String newQuery = query;

        // Skip substituting values equal to null
        for (Object arg : args) {
            if (arg != null)
                newQuery = newQuery.substring(0, newQuery.indexOf('?')) + getValueRepresentationByType(arg) + newQuery.substring(newQuery.indexOf('?') + 1);
        }

        return newQuery;
    }

    private static int countNotNullArguments(Object[] args) {
        return Arrays.stream(args).filter(arg -> arg != null).toArray(Object[]::new).length;
    }

    private static int countOccurrenceOfCharacterInString(String string, Character character) {
        return string.chars()
                .filter(c -> c == character)
                .toArray()
                .length;
    }

    public static String getValueRepresentationByType(Object value) {
        String className = value.getClass().getSimpleName();

        if (className.equals("String") || className.equals("Character"))
            return "'" + value + "'";

        return "" + value + "";
    }

    public static String extractParameterNameFromParameterWithComparison(String namedParameter) {
        ComparisonType cp = getComparisonType(namedParameter);

        return cp == null ? namedParameter : namedParameter.replace(cp.getOpName(), "");
    }

    public static ComparisonType getComparisonType(String property) {
        ComparisonType[] comparisons = ComparisonType.values();
        ComparisonType out = null;

        // Get the longest comparison match among the comparisons
        // Starting from the right
        int longest = 0;
        for (ComparisonType c : comparisons) {
            int i = 0;
            int currentMatch = 0;

            while (i < property.length() - 1 && i < c.getOpName().length() - 1) {
                if (property.charAt(property.length() - 1 - i) == c.getOpName().charAt(c.getOpName().length() - 1 - i)) {
                    currentMatch++;
                    i++;
                    if (currentMatch > longest) {
                        if (c.getOpName().length() - 1 == i) {
                            longest = currentMatch;
                            out = c;
                        }
                    }
                } else {
                    break;
                }
            }
        }

        return out;
    }

    public void printMap(Map<String, Object> map) {
        for (String key : map.keySet())
            System.out.println(key + ": " + map.get(key));
    }

}
