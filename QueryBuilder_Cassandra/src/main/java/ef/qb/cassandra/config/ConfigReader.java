package ef.qb.cassandra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ef.qb.cassandra.exceptions.WrongConfigurationException;
import java.io.File;
import java.net.URISyntaxException;
import static java.nio.file.Paths.get;
import static java.util.logging.Logger.getLogger;

public class ConfigReader {

    public static CassandraConfig getConfiguration() {
        return getConfiguration("config.json");
    }

    public static CassandraConfig getConfiguration(String resourcePath) {
        var objectMapper = new ObjectMapper();
        var res = ConfigReader.class.getClassLoader().getResource(resourcePath);

        if (res == null) {
            return getDefaultConfig();
        }

        File json;
        try {
            json = get(res.toURI()).toFile();
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
        var logger = getLogger("InfoLogging");
        logger.info("No valid \"config.json\" file found in resource folder, using default configuration.");

        var defaultConfig = new CassandraConfig();
        defaultConfig.setOrderingLimit(1000);
        defaultConfig.setSecondaryQueryLimit(3);

        return defaultConfig;
    }

}
