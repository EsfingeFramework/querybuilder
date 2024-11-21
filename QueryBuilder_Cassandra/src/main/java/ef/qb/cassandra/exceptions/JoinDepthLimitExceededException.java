package ef.qb.cassandra.exceptions;

public class JoinDepthLimitExceededException extends RuntimeException {

    public JoinDepthLimitExceededException(String message) {
        super(message);
    }
}
