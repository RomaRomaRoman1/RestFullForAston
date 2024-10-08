package org.example.exceptions.factory;

import java.time.LocalDateTime;

public record ErrorMessage(int statusCode,
                           LocalDateTime localDateTime,
                           String message,
                           String description) {
}
