package net.sf.esfinge.querybuilder.cassandra.integration.dbcommunication;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.KeyspaceRepository;
import net.sf.esfinge.querybuilder.cassandra.cassandrautils.ReplicationStrategy;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidNumberOfReplicasException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeyspaceRepositoryIntegrationTest {

    private final String KEYSPACE_NAME = "testkeyspace";

    private KeyspaceRepository schemaRepository;
    private Session session;

    @Before
    public void init() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initCassandraUnit();

        this.session = CassandraTestUtils.getSession();

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
        assertEquals(matchedKeyspaces.get(0), KEYSPACE_NAME.toLowerCase());
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

    @Test
    public void useKeyspaceTest() throws InvalidNumberOfReplicasException {
        schemaRepository.createKeyspace(KEYSPACE_NAME, ReplicationStrategy.SimpleStrategy, 1);

        schemaRepository.useKeyspace(KEYSPACE_NAME);

        // Query with no keyspace specified
        String query = "CREATE TABLE IF NOT EXISTS person(id int PRIMARY KEY, name text);";

        assertDoesNotThrow(() -> session.execute(query));
    }

    @After
    public void clean() {
        schemaRepository.deleteKeyspace(KEYSPACE_NAME);

        session.close();
    }

}