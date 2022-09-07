package net.sf.esfinge.querybuilder.cassandra.unit.config;

import net.sf.esfinge.querybuilder.cassandra.config.CassandraConfig;
import net.sf.esfinge.querybuilder.cassandra.config.ConfigReader;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigReaderTest {

    @Test
    public void cassandraSimpleConfigurationTest() {
        CassandraConfig config = ConfigReader.getConfiguration("config.json");

        assertEquals(10, config.getOrderingLimit());
        assertEquals(2, config.getSecondaryQueryLimit());
    }

    @Test
    public void cassandraDefaultConfigurationWhenNoConfigFileTest() {
        CassandraConfig config = ConfigReader.getConfiguration("nonexistent.json");

        assertEquals(1000, config.getOrderingLimit());
        assertEquals(3, config.getSecondaryQueryLimit());
    }

    @Test
    public void cassandraWrongConfigurationTest() {
        assertThrows(WrongConfigurationException.class, () -> ConfigReader.getConfiguration("wrongconfig.json"));
    }
}