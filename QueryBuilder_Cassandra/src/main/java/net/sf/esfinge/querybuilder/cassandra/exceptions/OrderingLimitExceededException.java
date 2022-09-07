package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class OrderingLimitExceededException extends RuntimeException {
    public OrderingLimitExceededException(String message) {
        super(message);
    }
}
