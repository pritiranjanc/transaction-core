package com.transaction.core.dto.response;

public record Error(
        String code,
        String message
) {
}
