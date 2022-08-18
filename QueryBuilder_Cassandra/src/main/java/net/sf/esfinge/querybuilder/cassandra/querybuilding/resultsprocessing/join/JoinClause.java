package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join;

import java.util.Objects;

public class JoinClause {

    private String joinTypeName;
    private String joinAttributeName;
    private JoinComparisonType comparisonType;

    private Object value;

    private int argPosition;

    public JoinClause(String joinTypeName, String joinAttributeName, JoinComparisonType comparisonType) {
        this.joinTypeName = joinTypeName;
        this.joinAttributeName = joinAttributeName;
        this.comparisonType = comparisonType;
    }

    public String getJoinTypeName() {
        return joinTypeName;
    }

    public void setJoinTypeName(String joinTypeName) {
        this.joinTypeName = joinTypeName;
    }

    public String getJoinAttributeName() {
        return joinAttributeName;
    }

    public void setJoinAttributeName(String joinAttributeName) {
        this.joinAttributeName = joinAttributeName;
    }

    public JoinComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(JoinComparisonType comparisonType) {
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

    @Override
    public String toString() {
        return "JoinClause{" +
                "joinTypeName='" + joinTypeName + '\'' +
                ", joinAttributeName='" + joinAttributeName + '\'' +
                ", comparisonType=" + comparisonType +
                ", value=" + value +
                ", argPosition=" + argPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinClause that = (JoinClause) o;
        return argPosition == that.argPosition && Objects.equals(joinTypeName, that.joinTypeName) && Objects.equals(joinAttributeName, that.joinAttributeName) && comparisonType == that.comparisonType && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinTypeName, joinAttributeName, comparisonType, value, argPosition);
    }
}
