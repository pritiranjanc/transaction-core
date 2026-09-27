package com.transaction.core.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageDTO<T> (
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
){
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
