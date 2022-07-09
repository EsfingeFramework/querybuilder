package net.sf.esfinge.querybuilder.cassandra.exceptions;

public class QueryParametersMismatchException extends RuntimeException{
    public QueryParametersMismatchException(String message) {
        super(message);
    }
}
