package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class MissingKeySpaceNameException extends RuntimeException{
    public MissingKeySpaceNameException(String message) {
        super(message);
    }
}
