package io.github.jotabrc.util;

import io.github.jotabrc.annotation.ServicePriority;

import java.util.Comparator;
import java.util.ServiceLoader;

public class DependencySelectorImpl implements DependencySelector {

    public static DependencySelector dependencySelector;

    private DependencySelectorImpl() {}

    @Override
    public <T> T select(Class<T> targetClass) {
        ServiceLoader<T> loader = getLoader(targetClass);
        return loader
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(targetClass::isInstance)
                .min(Comparator.comparingInt(e -> e.getClass().getAnnotation(ServicePriority.class).value()))
                .or(() ->
                        getLoader(targetClass)
                                .stream()
                                .map(ServiceLoader.Provider::get)
                                .filter(targetClass::isInstance)
                                .findFirst()
                )
                .orElseThrow(() -> new RuntimeException("Unable to find dependency"));
    }

    @Override
    public <T> ServiceLoader<T> getLoader(Class<T> targetClass) {
        return ServiceLoader.load(targetClass);
    }

    public static DependencySelector getInstance() {
        if (dependencySelector == null) dependencySelector = new DependencySelectorImpl();
        return dependencySelector;
    }
}
