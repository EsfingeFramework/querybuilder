package ef.qb.cassandra.exceptions;

public class NotEnoughExamplesException extends RuntimeException {

    public NotEnoughExamplesException(String message) {
        super(message);
    }
}
