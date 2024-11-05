package ef.qb.jpa1;

import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.conditions.NullOption;
import ef.qb.core.utils.StringUtils;

public class ConditionDescription {

    private final String propertyName;
    private final String paramName;
    private final ComparisonType compType;
    private Object fixedValue;
    private NullOption nullOption = NullOption.NONE;
    private String nextConector = "";

    public ConditionDescription(String propertyName, ComparisonType compType) {
        super();
        this.propertyName = propertyName;
        this.paramName = StringUtils.toCamelCase(propertyName) + compType.getOpName();
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

    public String getConditionString() {
        var sb = new StringBuilder();
        if (nullOption != NullOption.NONE) {
            sb.append("#{(").append(paramName).append(" != null)? '");
        }
        sb.append(" o.").append(propertyName).append(" ").append(compType.getOperator()).append(" :").append(paramName);
        if (nullOption == NullOption.COMPARE_TO_NULL) {
            sb.append("' : ' o.").append(propertyName).append(" IS NULL'}");
        } else if (nullOption == NullOption.IGNORE_WHEN_NULL) {
            sb.append("' : ''}");
        }
        return sb.toString();
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        this.fixedValue = fixedValue;
    }
}
