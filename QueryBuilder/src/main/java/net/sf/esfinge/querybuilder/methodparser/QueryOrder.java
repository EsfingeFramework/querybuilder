package net.sf.esfinge.querybuilder.methodparser;

public class QueryOrder {

    private final String property;
    private final OrderingDirection direction;

    public QueryOrder(String property, OrderingDirection diretion) {
        super();
        this.property = property;
        this.direction = diretion;
    }

    public String getProperty() {
        return property;
    }

    public OrderingDirection getDiretion() {
        return direction;
    }

    public void visit(QueryVisitor visitor) {
        visitor.visitOrderBy(property, direction);
    }

}
