package net.sf.esfinge.querybuilder.cassandra.cassandrautils;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidNumberOfReplicasException;

/**
 * Repository to handle the Cassandra schema.
 */
public class KeyspaceRepository {

    private final Session session;

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

        String sb = "CREATE KEYSPACE IF NOT EXISTS " +
                keyspaceName +
                " WITH replication = {" +
                "'class':'" +
                replicationStrategy.name() +
                "','replication_factor':" +
                numberOfReplicas + "};";

        session.execute(sb);
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

        final String query = "DROP KEYSPACE IF EXISTS " + keyspaceName;

        session.execute(query);
    }

}
