package ef.qb.cassandra.querybuilding;

import static ef.qb.cassandra.querybuilding.QueryBuildingUtils.getValueRepresentationByType;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.core.methodparser.conditions.NullOption;
import static ef.qb.core.methodparser.conditions.NullOption.IGNORE_WHEN_NULL;
import static ef.qb.core.methodparser.conditions.NullOption.NONE;

public class ConditionStatement extends Clause {

    private ComparisonType comparisonType;
    private NullOption nullOption = NONE;
    private String nextConnector = null;

    public ConditionStatement(String propertyName, ComparisonType comparisonType) {
        super(propertyName);
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

    public int getArgPosition() {
        return argPosition;
    }

    public void setArgPosition(int argPosition) {
        this.argPosition = argPosition;
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
        var sb = new StringBuilder();

        // Only append if NullOption is equal to NONE, otherwise
        // ignore condition
        if (nullOption == NONE) {
            sb.append(propertyName + " " + comparisonType.getOperator() + " " + getValueRepresentation());
        } else if (nullOption == IGNORE_WHEN_NULL) {
            if (value != null) {
                sb.append(propertyName + " " + comparisonType.getOperator() + " " + getValueRepresentation());
            }
        }

        return sb.toString();
    }

    public boolean isIgnoredCondition() {
        return nullOption == IGNORE_WHEN_NULL && value == null;
    }

    private String getValueRepresentation() {
        return value != null ? getValueRepresentationByType(value) : argPosition + "?";
    }

    @Override
    public String toString() {
        return getConditionRepresentation();
    }

}
