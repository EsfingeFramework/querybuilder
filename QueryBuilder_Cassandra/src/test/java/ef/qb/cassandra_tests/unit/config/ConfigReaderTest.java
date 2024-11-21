package ef.qb.cassandra_tests.unit.config;

import ef.qb.cassandra.config.CassandraConfig;
import static ef.qb.cassandra.config.ConfigReader.getConfiguration;
import ef.qb.cassandra.exceptions.WrongConfigurationException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigReaderTest {

    @Test
    public void cassandraSimpleConfigurationTest() {
        CassandraConfig config = getConfiguration("config.json");

        assertEquals(10, config.getOrderingLimit());
        assertEquals(2, config.getSecondaryQueryLimit());
    }

    @Test
    public void cassandraDefaultConfigurationWhenNoConfigFileTest() {
        CassandraConfig config = getConfiguration("nonexistent.json");

        assertEquals(1000, config.getOrderingLimit());
        assertEquals(3, config.getSecondaryQueryLimit());
    }

    @Test
    public void cassandraWrongConfigurationTest() {
        assertThrows(WrongConfigurationException.class, () -> getConfiguration("wrongconfig.json"));
    }
}