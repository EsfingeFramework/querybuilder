package ef.qb.cassandra.querybuilding;

import ef.qb.cassandra.exceptions.QueryParametersMismatchException;
import ef.qb.core.methodparser.ComparisonType;
import static ef.qb.core.methodparser.ComparisonType.values;
import java.util.Arrays;

public class QueryBuildingUtils {

    public static String replaceQueryArgs(String query, Object[] args) {
        var paramOccurrence = countOccurrenceOfCharacterInString(query, '?');
        var substituted = 0;
        var newQuery = query;

        // Skip substituting values equal to null. Arguments that have no placeholder are used in
        // SpecialComparison clauses and do not need to be substituted
        for (var i = 0; i < args.length; i++) {
            if (args[i] != null && newQuery.contains(i + "?")) {
                newQuery = newQuery.replace(i + "?", getValueRepresentationByType(args[i]));
                substituted++;
            }
        }

        if (paramOccurrence != substituted) {
            throw new QueryParametersMismatchException("Too many placeholders in the query \"" + query + "\" for arguments \"" + Arrays.toString(args) + "\"");
        }

        return newQuery;
    }

    public static int countOccurrenceOfCharacterInString(String string, Character character) {
        return string.chars()
                .filter(c -> c == character)
                .toArray().length;
    }

    public static String getValueRepresentationByType(Object value) {
        var className = value.getClass().getSimpleName();

        if (className.equals("String") || className.equals("Character")) {
            return "'" + value + "'";
        }

        return "" + value + "";
    }

    public static String extractParameterNameFromParameterWithComparison(String namedParameter) {
        var cp = getComparisonType(namedParameter);

        return cp == null ? namedParameter : namedParameter.replace(cp.getOpName(), "");
    }

    public static ComparisonType getComparisonType(String property) {
        var comparisons = values();
        ComparisonType out = null;

        // Get the longest comparison match among the comparisons
        // Starting from the right
        var longest = 0;
        for (var c : comparisons) {
            var i = 0;
            var currentMatch = 0;

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

}
