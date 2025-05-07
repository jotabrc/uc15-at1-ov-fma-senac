package io.github.jotabrc.config;

import io.github.jotabrc.util.LoadEnvironmentVariables;
import io.github.jotabrc.util.LoadEnvironmentVariablesImpl;
import io.github.jotabrc.util.PropertiesLoader;
import io.github.jotabrc.util.PropertiesLoaderImpl;

public class DependencyInjection {

    public static PropertiesLoader createPropertiesLoader() {
        return new PropertiesLoaderImpl();
    }

    public static LoadEnvironmentVariables createLoadEnvironmentVariables() {
        return new LoadEnvironmentVariablesImpl();
    }
}
