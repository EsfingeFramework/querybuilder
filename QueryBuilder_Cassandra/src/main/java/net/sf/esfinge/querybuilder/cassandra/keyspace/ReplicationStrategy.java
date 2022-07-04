package net.sf.esfinge.querybuilder.cassandra.keyspace;

public enum ReplicationStrategy {
    SimpleStrategy("SimpleStrategy"), NetworkTopologyStrategy("NetworkTopologyStrategy");

    private final String name;

    ReplicationStrategy(String s) {
        name = s;
    }
}
