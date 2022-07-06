package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class NotEnoughExamplesException extends RuntimeException {
    public NotEnoughExamplesException(String message) {
        super(message);
    }
}
