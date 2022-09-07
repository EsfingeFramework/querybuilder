package net.sf.esfinge.querybuilder.cassandra.config;

public class CassandraConfig {

    private int orderingLimit;
    private int secondaryQueryLimit;

    public int getOrderingLimit() {
        return orderingLimit;
    }

    public void setOrderingLimit(int orderingLimit) {
        this.orderingLimit = orderingLimit;
    }

    public int getSecondaryQueryLimit() {
        return secondaryQueryLimit;
    }

    public void setSecondaryQueryLimit(int secondaryQueryLimit) {
        this.secondaryQueryLimit = secondaryQueryLimit;
    }
}
