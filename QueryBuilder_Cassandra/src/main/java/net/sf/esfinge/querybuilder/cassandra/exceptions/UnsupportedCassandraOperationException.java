package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class UnsupportedCassandraOperationException extends RuntimeException {
    public UnsupportedCassandraOperationException(String message) {
        super(message);
    }
}
