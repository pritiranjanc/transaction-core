package com.transaction.core.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PageDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageDTO<T> from(List<T> content, int totalPages,int page ,int size,long totalElements){
        return PageDTO.<T>builder()
                .content(content)
                .totalPages(totalPages)
                .page(page)
                .totalElements(totalElements)
                .size(size)
                .build();
    }
}
