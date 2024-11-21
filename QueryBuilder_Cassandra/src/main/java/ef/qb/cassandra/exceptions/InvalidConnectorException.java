package ef.qb.cassandra.exceptions;

public class InvalidConnectorException extends RuntimeException {

    public InvalidConnectorException(String message) {
        super(message);
    }
}
