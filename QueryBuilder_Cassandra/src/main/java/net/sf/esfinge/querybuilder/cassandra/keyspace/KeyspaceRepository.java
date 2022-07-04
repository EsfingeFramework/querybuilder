package net.sf.esfinge.querybuilder.cassandra.keyspace;

import com.datastax.driver.core.Session;

/**
 * Repository to handle the Cassandra schema.
 */
public class KeyspaceRepository {

    private Session session;

    public KeyspaceRepository(Session session) {
        this.session = session;
    }

    /**
     * Method used to create any keyspace - schema.
     *
     * @param keyspaceName        the name of the schema.
     * @param replicationStrategy the replication strategy,
     *                            either SimpleStrategy or NetworkTopologyStrategy
     * @param numberOfReplicas    the number of replicas.
     */
    public void createKeyspace(String keyspaceName, ReplicationStrategy replicationStrategy, int numberOfReplicas) throws InvalidNumberOfReplicasException {
        if (numberOfReplicas < 1)
            throw new InvalidNumberOfReplicasException("Invalid number of replicas: " + numberOfReplicas);

        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName)
                .append(" WITH replication = {")
                .append("'class':'")
                .append(replicationStrategy.name())
                .append("','replication_factor':")
                .append(numberOfReplicas).append("};");

        final String query = sb.toString();

        session.execute(query);
    }

    public void useKeyspace(String keyspace) {
        session.execute("USE " + keyspace);
    }

    /**
     * Method used to delete the specified schema.
     * It results in the immediate, irreversible removal of the keyspace, including all tables and data contained in the keyspace.
     *
     * @param keyspaceName the name of the keyspace to delete.
     */
    public void deleteKeyspace(String keyspaceName) {
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ").append(keyspaceName);

        final String query = sb.toString();

        session.execute(query);
    }
}
