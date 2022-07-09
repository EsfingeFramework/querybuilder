package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.cassandra.exceptions.QueryParametersMismatchException;

public class QueryBuildingUtilities {

    public static String replaceQueryArgs(String query, Object[] args){
        int[] paramIndexes = getIndexesOfParamsInString(query);

        if (paramIndexes.length != args.length)
            throw new QueryParametersMismatchException("Number of parameters in the query different from the number of arguments");

        String newQuery = query;

        int current = 0;

        for (int index : paramIndexes){
            newQuery = newQuery.substring(0,index) + getValueRepresentationByType(args[current]) + newQuery.substring(index + 1);
            current++;
        }

        return newQuery;
    }
    private static int[] getIndexesOfParamsInString(String string){
        int occurrence = countOccurrenceOfCharacterInString(string, '?');

        int[] indexes = new int[occurrence];

        int current = 0;

        for (int i = 0; i < string.length(); i++){
            if (string.charAt(i) == '?'){
                indexes[current] = i;
                current++;
            }
        }

        return indexes;
    }

    private static int countOccurrenceOfCharacterInString(String string, Character character){
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
}
