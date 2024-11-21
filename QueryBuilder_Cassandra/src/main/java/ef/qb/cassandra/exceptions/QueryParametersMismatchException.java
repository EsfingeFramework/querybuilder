package ef.qb.cassandra.exceptions;

public class QueryParametersMismatchException extends RuntimeException {

    public QueryParametersMismatchException(String message) {
        super(message);
    }
}
