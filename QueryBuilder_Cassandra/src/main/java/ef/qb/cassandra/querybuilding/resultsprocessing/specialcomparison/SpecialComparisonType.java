package ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;

import ef.qb.cassandra.exceptions.ComparisonTypeNotFoundException;
import ef.qb.core.methodparser.ComparisonType;

public enum SpecialComparisonType {
    NOT_EQUALS("NotEquals"), CONTAINS("Contains"), STARTS("Starts"), ENDS("Ends"), COMPARE_TO_NULL("Equals");

    private String opName;

    public String getOpName() {
        return this.opName;
    }

    private SpecialComparisonType(String name) {
        this.opName = name;
    }

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
