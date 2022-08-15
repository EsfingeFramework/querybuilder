package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

public class ConditionStatement {

    private String propertyName;
    private ComparisonType comparisonType;
    private Object value = null;

    private int conditionIndex = -1;
    private NullOption nullOption = NullOption.NONE;
    private String nextConnector = null;

    public ConditionStatement(String propertyName, ComparisonType comparisonType) {
        this.propertyName = propertyName;
        this.comparisonType = comparisonType;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getConditionIndex() {
        return conditionIndex;
    }

    public void setConditionIndex(int conditionIndex) {
        this.conditionIndex = conditionIndex;
    }

    public NullOption getNullOption() {
        return nullOption;
    }

    public void setNullOption(NullOption nullOption) {
        this.nullOption = nullOption;
    }

    public String getNextConnector() {
        return nextConnector;
    }

    public void setNextConnector(String nextConnector) {
        this.nextConnector = nextConnector.toUpperCase();
    }

    private String getConditionRepresentation() {
        StringBuilder sb = new StringBuilder();

        // Only append if NullOption is equal to NONE, otherwise
        // ignore condition
        if (nullOption == NullOption.NONE) {
            sb.append(propertyName + " " + comparisonType.getOperator() + " " + getValueRepresentation());
        } else if (nullOption == NullOption.IGNORE_WHEN_NULL) {
            if (value != null)
                sb.append(propertyName + " " + comparisonType.getOperator() + " " + getValueRepresentation());
        }

        return sb.toString();
    }

    public boolean isIgnoredCondition() {
        return nullOption == NullOption.IGNORE_WHEN_NULL && value == null;
    }

    private String getValueRepresentation() {
        return value != null ? QueryBuildingUtils.getValueRepresentationByType(value) : conditionIndex + "?";
    }

    @Override
    public String toString() {
        return getConditionRepresentation();
    }


}
