package org.example.restaurantvoting.common.exception;


import static org.example.restaurantvoting.common.exception.ErrorType.NOT_FOUND;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg, NOT_FOUND);
    }
}