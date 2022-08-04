package net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering;

import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;

import java.util.Objects;

public class OrderByClause {
    String propertyName;
    OrderingDirection direction;

    public OrderByClause(String propertyName, OrderingDirection direction) {
        this.propertyName = propertyName;
        this.direction = direction;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public OrderingDirection getDirection() {
        return direction;
    }

    public void setDirection(OrderingDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "OrderByClause{" +
                "propertyName='" + propertyName + '\'' +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderByClause that = (OrderByClause) o;
        return Objects.equals(propertyName, that.propertyName) && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, direction);
    }
}
