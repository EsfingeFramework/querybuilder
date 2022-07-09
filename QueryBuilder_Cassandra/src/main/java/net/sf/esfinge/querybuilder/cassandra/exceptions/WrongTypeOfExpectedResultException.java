package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class WrongTypeOfExpectedResultException extends RuntimeException {
    public WrongTypeOfExpectedResultException(String message) {
        super(message);
    }
}
