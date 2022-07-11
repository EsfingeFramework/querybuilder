package net.sf.esfinge.querybuilder.cassandra.cassandrautils;

public enum ReplicationStrategy {
    SimpleStrategy("SimpleStrategy"), NetworkTopologyStrategy("NetworkTopologyStrategy");

    ReplicationStrategy(String s) {
    }
}
