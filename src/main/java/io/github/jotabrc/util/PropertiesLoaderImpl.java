package io.github.jotabrc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoaderImpl implements PropertiesLoader {

    @Override
    public Properties load(String path) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(stream);

            return properties;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
