package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

public class ConditionStatement {

    private String propertyName;
    private ComparisonType comparisonType;

    private Object value = null;

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
        this.nextConnector = nextConnector;
    }

    private String getValueRepresentation(){
        if (value.getClass().getSimpleName().equals("String"))
            return "'" + value + "'";

        return "" + value + "";
    }

    @Override
    public String toString() {


        return propertyName + " " + comparisonType.getOperator() + " " + (value != null ? getValueRepresentation() : "?");

    }


}
