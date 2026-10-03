package com.transaction.core.dto.response;

public record ErrorDTO(
        String code,
        String message
) {
}
