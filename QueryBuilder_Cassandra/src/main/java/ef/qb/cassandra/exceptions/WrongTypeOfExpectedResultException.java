package ef.qb.cassandra.exceptions;

public class WrongTypeOfExpectedResultException extends RuntimeException {

    public WrongTypeOfExpectedResultException(String message) {
        super(message);
    }
}
