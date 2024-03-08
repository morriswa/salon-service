package org.morriswa.salon.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class ServiceInfoFactoryImpl implements ServiceInfoFactory {


    private final String APPLICATION_NAME;
    private final String APPLICATION_VERSION;

    @Autowired
    public ServiceInfoFactoryImpl(BuildProperties build) {
        this.APPLICATION_NAME = build.getName();
        this.APPLICATION_VERSION = build.getVersion();
    }

    public ResponseEntity<ServiceInfoResponse> getHttpResponseWithServiceInfo(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ServiceInfoResponse(
                    message, ZonedDateTime.now(), this.APPLICATION_NAME, this.APPLICATION_VERSION)
                );
    }

    public ResponseEntity<ServiceErrorResponse> getHttpErrorResponse(HttpStatus status, String exceptionName, String description) {
        return ResponseEntity
                .status(status)
                .body(new ServiceErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, null));
    }

    public ResponseEntity<ServiceErrorResponse> getHttpErrorResponse(HttpStatus status, String exceptionName, String description, Object additionalInfo) {
        return ResponseEntity
                .status(status)
                .body(new ServiceErrorResponse(exceptionName, description, ZonedDateTime.now(),
                        APPLICATION_NAME, APPLICATION_VERSION, additionalInfo));
    }
}
