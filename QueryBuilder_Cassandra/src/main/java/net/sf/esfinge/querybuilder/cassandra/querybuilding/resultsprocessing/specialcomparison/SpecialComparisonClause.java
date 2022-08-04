package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import java.util.Objects;

public class SpecialComparisonClause {

    private String propertyName;

    private SpecialComparisonType specialComparisonType;

    private Object value;

    private int argPosition;

    public SpecialComparisonClause(String propertyName, SpecialComparisonType specialComparisonType) {
        this.propertyName = propertyName;
        this.specialComparisonType = specialComparisonType;
        this.value = null;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public SpecialComparisonType getSpecialComparisonType() {
        return specialComparisonType;
    }

    public void setSpecialComparisonType(SpecialComparisonType specialComparisonType) {
        this.specialComparisonType = specialComparisonType;
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

    @Override
    public String toString() {
        return "SpecialComparisonClause{" +
                "propertyName='" + propertyName + '\'' +
                ", specialComparisonType=" + specialComparisonType +
                ", value=" + value +
                ", argPosition=" + argPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialComparisonClause that = (SpecialComparisonClause) o;
        return Objects.equals(propertyName, that.propertyName) && specialComparisonType == that.specialComparisonType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, specialComparisonType);
    }
}
