package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.exceptions.ComparisonTypeNotFoundException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public enum SpecialComparisonType {
    NOT_EQUALS, CONTAINS, STARTS, ENDS;

    public static SpecialComparisonType fromComparisonType(ComparisonType comparisonType) {
        switch (comparisonType) {
            case NOT_EQUALS:
                return SpecialComparisonType.NOT_EQUALS;
            case CONTAINS:
                return SpecialComparisonType.CONTAINS;
            case STARTS:
                return SpecialComparisonType.STARTS;
            case ENDS:
                return SpecialComparisonType.ENDS;
            default:
                throw new ComparisonTypeNotFoundException("Comparison type \"" + comparisonType.name() + "\" cannot be converted to SpecialComparisonType");
        }
    }

}
