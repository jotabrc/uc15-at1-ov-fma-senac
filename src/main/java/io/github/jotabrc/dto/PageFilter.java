package io.github.jotabrc.dto;

import java.time.LocalDate;
import java.util.StringJoiner;

public record PageFilter (String userUuid, LocalDate fromDate, LocalDate toDate, int pageNumber, int pageSize, String sort) {

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"userUuid\":" + userUuid + "\"")
                .add("\"fromDate\":\"" + fromDate + "\"")
                .add("\"toDate\":\"" + toDate + "\"")
                .add("\"pageNumber\":" + pageNumber)
                .add("\"pageSize\":" + pageSize)
                .add("\"sort\":\"" + sort + "\"")
                .toString();
    }
}
