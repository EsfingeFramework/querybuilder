package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class SecondaryQueryLimitExceededException extends RuntimeException {
    public SecondaryQueryLimitExceededException(String message) {
        super(message);
    }
}
