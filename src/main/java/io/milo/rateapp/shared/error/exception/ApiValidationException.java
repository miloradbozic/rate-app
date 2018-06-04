package io.milo.rateapp.shared.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Validation error")
public class ApiValidationException extends RuntimeException {
    public ApiValidationException() {
    }

    public ApiValidationException(Exception e) {
        super(e);
    }

    public ApiValidationException(String message) {
        super(message);
    }
}