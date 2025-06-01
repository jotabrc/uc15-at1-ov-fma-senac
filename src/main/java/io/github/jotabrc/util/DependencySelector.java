package io.github.jotabrc.util;

import java.util.ServiceLoader;

public interface DependencySelector {

    <T> T select(Class<T> clazz);
    <T> ServiceLoader<T> getLoader(Class<T> clazz);
}
