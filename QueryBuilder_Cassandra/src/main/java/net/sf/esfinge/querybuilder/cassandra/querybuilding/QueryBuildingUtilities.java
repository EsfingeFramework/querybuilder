package net.sf.esfinge.querybuilder.cassandra.querybuilding;

public class QueryBuildingUtilities {

    public static String replaceQueryArgs(String query, Object[] args){
        int[] paramIndexes = getIndexesOfSubstringInString(query,"?");

        return "";
    }
    private static int[] getIndexesOfSubstringInString(String string, String substring){
         System.out.println(string.split(substring, -1).length-1);

        return new int[1];
    }
}
