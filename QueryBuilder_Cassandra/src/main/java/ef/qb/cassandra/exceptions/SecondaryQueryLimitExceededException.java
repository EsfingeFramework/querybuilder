package ef.qb.cassandra.exceptions;

public class SecondaryQueryLimitExceededException extends RuntimeException {

    public SecondaryQueryLimitExceededException(String message) {
        super(message);
    }
}
