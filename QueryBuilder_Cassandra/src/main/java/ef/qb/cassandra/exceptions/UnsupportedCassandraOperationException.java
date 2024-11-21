package ef.qb.cassandra.exceptions;

public class UnsupportedCassandraOperationException extends RuntimeException {

    public UnsupportedCassandraOperationException(String message) {
        super(message);
    }
}
