package ef.qb.core.exception;

public class InvalidPropertyException extends RuntimeException {

    private static final long serialVersionUID = 5868753317297365073L;

    public InvalidPropertyException(String message) {
        super(message);
    }

    public InvalidPropertyException(String message, Throwable ex) {
        super(message, ex);
    }

}
