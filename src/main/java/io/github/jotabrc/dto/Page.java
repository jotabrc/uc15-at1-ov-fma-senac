package io.github.jotabrc.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {

    private final int pageNumber;
    private final int pageSize;
    private final String sort;
    private final List<T> content;

    public Page(int pageNumber, int pageSize, String sort, List<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
        this.content = content;
    }
}
