package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public class ConditionStatement {

    private String propertyName;
    private ComparisonType comparisonType;
    private String nextConnector;

    public ConditionStatement(String propertyName, ComparisonType comparisonType) {
        this.propertyName = propertyName;
        this.comparisonType = comparisonType;
        this.nextConnector = "";
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getNextConnector() {
        return nextConnector;
    }

    public void setNextConnector(String nextConnector) {
        this.nextConnector = nextConnector;
    }

    @Override
    public String toString() {
        return propertyName + " " + comparisonType.getOperator() + " ?";

    }
}
