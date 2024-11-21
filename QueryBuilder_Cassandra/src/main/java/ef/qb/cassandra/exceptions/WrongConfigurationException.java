package ef.qb.cassandra.exceptions;

public class WrongConfigurationException extends RuntimeException {

    public WrongConfigurationException(String message) {
        super(message);
    }
}
