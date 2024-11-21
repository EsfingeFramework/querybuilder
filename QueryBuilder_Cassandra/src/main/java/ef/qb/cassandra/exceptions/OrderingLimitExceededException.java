package ef.qb.cassandra.exceptions;

public class OrderingLimitExceededException extends RuntimeException {

    public OrderingLimitExceededException(String message) {
        super(message);
    }
}
