package net.sf.esfinge.querybuilder.cassandra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongConfigurationException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ConfigReader {

    public static CassandraConfig getConfiguration() {
        return getConfiguration("config.json");
    }

    public static CassandraConfig getConfiguration(String resourcePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        URL res = ConfigReader.class.getClassLoader().getResource(resourcePath);

        if (res == null)
            return getDefaultConfig();

        File json;
        try {
            json = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        CassandraConfig config;

        try {
            config = objectMapper.readValue(json, CassandraConfig.class);
        } catch (Exception e) {
            throw new WrongConfigurationException(e.getMessage());
        }

        return config;
    }

    private static CassandraConfig getDefaultConfig() {
        Logger logger = Logger.getLogger("InfoLogging");
        logger.info("No valid \"config.json\" file found in resource folder, using default configuration.");

        CassandraConfig defaultConfig = new CassandraConfig();
        defaultConfig.setOrderingLimit(1000);

        return defaultConfig;
    }


}
