package io.milo.rateapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends Exception {
    public NotFoundException() {
    }

    public NotFoundException(Exception e) {
        super(e);
    }
}