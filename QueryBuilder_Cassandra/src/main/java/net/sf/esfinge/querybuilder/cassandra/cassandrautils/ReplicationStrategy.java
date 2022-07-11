package net.sf.esfinge.querybuilder.cassandra.cassandrautils;

public enum ReplicationStrategy {
    SimpleStrategy("SimpleStrategy"), NetworkTopologyStrategy("NetworkTopologyStrategy");

    private final String name;

    ReplicationStrategy(String s) {
        name = s;
    }
}
