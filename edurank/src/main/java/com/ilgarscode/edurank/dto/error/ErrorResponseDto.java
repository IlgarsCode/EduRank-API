package com.ilgarscode.edurank.dto.error;

import java.time.LocalDateTime;

public class ErrorResponseDto {

    private String message;
    private LocalDateTime timestamp;

    public ErrorResponseDto(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}