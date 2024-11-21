package ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;

import ef.qb.cassandra.exceptions.MethodInvocationException;
import ef.qb.cassandra.querybuilding.Clause;
import static ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonUtils.hasCompareToNullAnnotationOnFields;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetterForField;
import static ef.qb.cassandra.reflection.CassandraReflectionUtils.getClassGetters;
import java.lang.reflect.Method;
import java.util.Objects;
import static java.util.Objects.hash;

public class SpecialComparisonClause extends Clause {

    private SpecialComparisonType specialComparisonType;

    public SpecialComparisonClause(String propertyName, SpecialComparisonType specialComparisonType) {
        super(propertyName);
        this.specialComparisonType = specialComparisonType;
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
        if (hasCompareToNullAnnotationOnFields(this.value)) {
            Method[] getters = getClassGetters(this.value.getClass());
            Method getter = getClassGetterForField(this.value.getClass(), getters, propertyName);

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
        return "SpecialComparisonClause{"
                + "propertyName='" + propertyName + '\''
                + ", specialComparisonType=" + specialComparisonType
                + ", value=" + value
                + ", argPosition=" + argPosition
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecialComparisonClause that = (SpecialComparisonClause) o;
        return Objects.equals(propertyName, that.propertyName) && specialComparisonType == that.specialComparisonType;
    }

    @Override
    public int hashCode() {
        return hash(propertyName, specialComparisonType);
    }
}
