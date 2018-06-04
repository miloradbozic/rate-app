package io.milo.rateapp.shared.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class ApiEntityNotFoundException extends RuntimeException {
    public ApiEntityNotFoundException() {
    }

    public ApiEntityNotFoundException(Exception e) {
        super(e);
    }

    public ApiEntityNotFoundException(String message) {
        super(message);
    }
}