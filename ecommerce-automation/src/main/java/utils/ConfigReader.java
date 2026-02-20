package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {
	
	private static Properties properties;
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";

    static {
        loadProperties();
    }
    
    // Load properties from configuration file
    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + e.getMessage());
            throw new RuntimeException("Configuration file not found at: " + CONFIG_FILE_PATH);
        }
    }
    
    /**
     * Get property value by key
     * @param key property key
     * @return property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
        }
        return value;
    }
    
    /**
     * Get property value with default fallback
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
