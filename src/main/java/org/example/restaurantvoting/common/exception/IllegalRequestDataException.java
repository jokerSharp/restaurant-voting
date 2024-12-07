package org.example.restaurantvoting.common.exception;


import static org.example.restaurantvoting.common.exception.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}