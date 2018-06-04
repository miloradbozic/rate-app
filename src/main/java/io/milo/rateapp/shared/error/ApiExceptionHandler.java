package io.milo.rateapp.shared.error;

import io.milo.rateapp.shared.error.exception.ApiElasticResponseException;
import io.milo.rateapp.shared.error.exception.ApiEntityNotFoundException;
import io.milo.rateapp.shared.error.exception.ApiValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public ApiExceptionHandler() {
        super();
    }

    @ExceptionHandler({ ApiEntityNotFoundException.class })
    public ResponseEntity<ApiError> handleEntityNotFoundException(ApiEntityNotFoundException ex) {
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                "Entity not found", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ApiValidationException.class })
    public ResponseEntity<ApiError> handleValidationException(ApiValidationException ex) {
        final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                "Validation exception", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ApiElasticResponseException.class })
    public ResponseEntity<ApiError> handleElasticRequestException(ApiValidationException ex) {
        final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
                "Elasticsearch request exception", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}