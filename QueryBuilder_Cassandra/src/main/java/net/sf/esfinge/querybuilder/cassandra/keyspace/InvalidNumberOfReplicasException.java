package net.sf.esfinge.querybuilder.cassandra.keyspace;

public class InvalidNumberOfReplicasException extends Exception {

    public InvalidNumberOfReplicasException(String message) {
        super(message);
    }
}
