package org.example.restaurantvoting.common.exception;

import lombok.Getter;
import org.springframework.lang.NonNull;

public class AppException extends RuntimeException {

    @Getter
    private final ErrorType errorType;

    public AppException(@NonNull String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
