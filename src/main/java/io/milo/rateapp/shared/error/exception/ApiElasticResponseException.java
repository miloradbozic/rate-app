package io.milo.rateapp.shared.error.exception;

public class ApiElasticResponseException extends RuntimeException {
    public ApiElasticResponseException() {}
    public ApiElasticResponseException(Exception e) {
        super(e);
    }
    public ApiElasticResponseException(String message) {
        super(message);
    }
}