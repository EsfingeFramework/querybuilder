package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison;

import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.reflection.CassandraReflectionUtils;

import java.lang.reflect.Method;
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
        if (SpecialComparisonUtils.hasCompareToNullAnnotationOnFields(this.value)) {
            Method[] getters = CassandraReflectionUtils.getClassGetters(this.value.getClass());
            Method getter = CassandraReflectionUtils.getClassGetterForField(this.value.getClass(), getters, propertyName);

            try {
                return getter.invoke(this.value);
            } catch (Exception e) {
                throw new MethodInvocationException("Could not invoke method \"" + getter.getName() + "\" on object \"" + this.value + "\", this is caused by: " + e.getMessage());
            }
        }

        return this.value;
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
