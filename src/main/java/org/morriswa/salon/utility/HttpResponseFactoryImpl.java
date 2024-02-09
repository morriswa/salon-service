package org.morriswa.salon.utility;

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
    private record ServiceInfoResponse(
        String message,
        ZonedDateTime timestamp,
        String applicationName,
        String version
    ) { }

    /**
     * Default Service Response when an error needs to be reported
     */
    private record ServiceErrorResponse(
        String error,
        String description,
        ZonedDateTime timestamp,
        String applicationName,
        String version,
        Object additionalInfo
    ) { }

    private final String APPLICATION_NAME;
    private final String APPLICATION_VERSION;

    @Autowired
    public HttpResponseFactoryImpl(BuildProperties build) {
        this.APPLICATION_NAME = build.getName();
        this.APPLICATION_VERSION = build.getVersion();
    }

    public ResponseEntity<?> serviceInfo(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ServiceInfoResponse(
                    message, ZonedDateTime.now(), this.APPLICATION_NAME, this.APPLICATION_VERSION)
                );
    }

    public ResponseEntity<?> error(HttpStatus status, String exceptionName, String description) {
        return ResponseEntity
                .status(status)
                .body(new ServiceErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, null));
    }

    public ResponseEntity<?> error(HttpStatus status, String exceptionName, String description, Object additionalInfo) {
        return ResponseEntity
                .status(status)
                .body(new ServiceErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, additionalInfo));
    }
}
