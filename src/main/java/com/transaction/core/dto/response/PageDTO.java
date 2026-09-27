package com.transaction.core.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

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

    public static <T> PageDTO<T> buildPage(List<T> content, Page<?> page){
        return PageDTO.<T>builder()
                .content(content)
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .build();
    }
}
