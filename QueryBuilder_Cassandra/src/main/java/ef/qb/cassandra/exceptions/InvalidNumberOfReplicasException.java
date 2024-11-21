package ef.qb.cassandra.exceptions;

public class InvalidNumberOfReplicasException extends RuntimeException {

    public InvalidNumberOfReplicasException(String message) {
        super(message);
    }
}
