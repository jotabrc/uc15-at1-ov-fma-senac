package io.github.jotabrc.repository.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DQML {

    INSERT("INSERT"),
    UPDATE("UPDATE"),
    SELECT("SELECT");

    private final String type;
}
