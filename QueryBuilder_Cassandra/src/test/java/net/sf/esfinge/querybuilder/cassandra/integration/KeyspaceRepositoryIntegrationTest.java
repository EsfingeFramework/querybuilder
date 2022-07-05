package net.sf.esfinge.querybuilder.cassandra.integration;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidNumberOfReplicasException;
import net.sf.esfinge.querybuilder.cassandra.keyspace.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.keyspace.ReplicationStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeyspaceRepositoryIntegrationTest {

    private static final String KEYSPACE_NAME = "persons";

    private KeyspaceRepository schemaRepository;
    private Session session;

    @Before
    public void init() {
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        client.connect();

        this.session = client.getSession();
        schemaRepository = new KeyspaceRepository(session);
    }

    @Test
    public void createKeyspaceTest() throws InvalidNumberOfReplicasException {
        schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 1);

        ResultSet result =
                session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(KEYSPACE_NAME.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        assertEquals(matchedKeyspaces.size(), 1);
        assertTrue(matchedKeyspaces.get(0).equals(KEYSPACE_NAME.toLowerCase()));
    }


    @Test
    public void createKeyspaceWithInvalidReplicasTest() {
        assertThrows(InvalidNumberOfReplicasException.class, () -> schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 0));
    }

    @Test
    public void deleteKeyspaceTest() throws InvalidNumberOfReplicasException {
        schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 1);
        schemaRepository.deleteKeyspace(KEYSPACE_NAME);

        ResultSet result =
                session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> matchedKeyspaces = result.all()
                .stream()
                .filter(r -> r.getString(0).equals(KEYSPACE_NAME.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());

        assertEquals(matchedKeyspaces.size(), 0);
    }

    @AfterAll
    public void clean() {
        schemaRepository.deleteKeyspace(KEYSPACE_NAME);
    }

}