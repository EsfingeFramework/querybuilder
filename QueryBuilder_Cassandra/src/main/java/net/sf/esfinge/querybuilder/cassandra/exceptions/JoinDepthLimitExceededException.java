package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class JoinDepthLimitExceededException extends RuntimeException {
    public JoinDepthLimitExceededException(String message) {
        super(message);
    }
}
