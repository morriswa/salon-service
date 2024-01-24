package org.morriswa.eecs447.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class HttpResponseFactoryImpl implements HttpResponseFactory {

    /**
     * Default Service Response
     */
    private record DefaultResponse<T> (
        String message,
        ZonedDateTime timestamp,
        String applicationName,
        String version,
        T payload
    ) { }

    /**
     * Default Service Response when an error needs to be reported
     */
    private record DefaultErrorResponse (
        String error,
        String description,
        ZonedDateTime timestamp,
        String applicationName,
        String version,
        Object stack
    ) { }

    private final String APPLICATION_NAME;
    private final String APPLICATION_VERSION;

    @Autowired
    public HttpResponseFactoryImpl(BuildProperties build) {
        this.APPLICATION_NAME = build.getName();
        this.APPLICATION_VERSION = build.getVersion();
    }

    public ResponseEntity<?> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new DefaultResponse<>(
                    message, ZonedDateTime.now(), this.APPLICATION_NAME, this.APPLICATION_VERSION, null)
                );
    }

    public ResponseEntity<?> build(HttpStatus status, String message, Object payload) {
        return ResponseEntity
                .status(status)
                .body(new DefaultResponse<>(
                        message, ZonedDateTime.now(), this.APPLICATION_NAME, this.APPLICATION_VERSION, payload
                ));
    }

    public ResponseEntity<?> error(HttpStatus status, String exceptionName, String description) {
        return ResponseEntity
                .status(status)
                .body(new DefaultErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, null));
    }

    public ResponseEntity<?> error(HttpStatus status, String exceptionName, String description, Object stack) {
        return ResponseEntity
                .status(status)
                .body(new DefaultErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, stack));
    }
}
