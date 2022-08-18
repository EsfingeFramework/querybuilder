package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import net.sf.esfinge.querybuilder.cassandra.exceptions.ComparisonTypeNotFoundException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public enum JoinComparisonType {
    EQUALS, GREATER_OR_EQUALS, LESSER_OR_EQUALS, GREATER, LESSER, NOT_EQUALS, CONTAINS, STARTS, ENDS, COMPARE_TO_NULL;

    public static JoinComparisonType fromComparisonType(ComparisonType comparisonType) {
        switch (comparisonType) {
            case EQUALS:
                return JoinComparisonType.EQUALS;
            case GREATER_OR_EQUALS:
                return JoinComparisonType.GREATER_OR_EQUALS;
            case LESSER_OR_EQUALS:
                return JoinComparisonType.LESSER_OR_EQUALS;
            case GREATER:
                return JoinComparisonType.GREATER;
            case LESSER:
                return JoinComparisonType.LESSER;
            case NOT_EQUALS:
                return JoinComparisonType.NOT_EQUALS;
            case CONTAINS:
                return JoinComparisonType.CONTAINS;
            case STARTS:
                return JoinComparisonType.STARTS;
            case ENDS:
                return JoinComparisonType.ENDS;
            default:
                throw new ComparisonTypeNotFoundException("Comparison type \"" + comparisonType.name() + "\" cannot be converted to JoinComparisonType");
        }
    }

}
