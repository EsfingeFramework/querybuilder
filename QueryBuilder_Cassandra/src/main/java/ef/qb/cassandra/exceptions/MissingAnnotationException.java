package ef.qb.cassandra.exceptions;

public class MissingAnnotationException extends RuntimeException {

    public MissingAnnotationException(String message) {
        super(message);
    }
}
