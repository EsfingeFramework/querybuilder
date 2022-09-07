package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class InvalidConnectorException extends RuntimeException {
    public InvalidConnectorException(String message) {
        super(message);
    }
}
