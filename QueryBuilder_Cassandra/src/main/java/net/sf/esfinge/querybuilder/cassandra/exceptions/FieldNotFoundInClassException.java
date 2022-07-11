package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class FieldNotFoundInClassException extends RuntimeException {
    public FieldNotFoundInClassException(String message) {
        super(message);
    }
}
