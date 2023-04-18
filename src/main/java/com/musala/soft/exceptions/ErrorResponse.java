package com.musala.soft.exceptions;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {

    private String message;
    private List<String> details;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
