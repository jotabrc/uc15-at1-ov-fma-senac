package io.github.jotabrc.config;

import io.github.jotabrc.util.PropertiesLoader;
import io.github.jotabrc.util.PropertiesLoaderImpl;

public class DependencyInjection {

    public static PropertiesLoader createPropertiesLoader() {
        return new PropertiesLoaderImpl();
    }
}
