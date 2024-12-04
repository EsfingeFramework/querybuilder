package ef.qb.neo4j;

import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.conditions.NullOption;

public class ConditionDescription {

    private String propertyName;
    private String paramName;
    private ComparisonType compType;
    private Object fixedValue;
    private NullOption nullOption = NullOption.NONE;
    private String nextConector = "";

    public ConditionDescription(String propertyName, ComparisonType compType) {
        super();
        this.propertyName = propertyName;
        this.paramName = propertyName.substring(propertyName.lastIndexOf(".") + 1);
        this.compType = compType;
    }

    public NullOption getNullOption() {
        return nullOption;
    }

    public void setNullOption(NullOption nullOption) {
        this.nullOption = nullOption;
    }

    public String getNextConector() {
        return nextConector;
    }

    public void setNextConector(String nextConector) {
        this.nextConector = nextConector;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getParamName() {
        return paramName;
    }

    public ComparisonType getCompType() {
        return compType;
    }

    public String getCondition() {
        throw new UnsupportedOperationException();
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        paramName += compType.name();
        this.fixedValue = fixedValue;
    }

    @Override
    public String toString() {
        var s = propertyName;
        s += " (" + paramName + ") ";
        s += compType + " ";
        return s;
    }
}
