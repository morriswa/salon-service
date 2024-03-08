package org.morriswa.salon.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

/**
 *  AUTHOR: William A. Morris <br>
 *  CREATION_DATE: 2024-01-19 <br>
 *  PURPOSE: <br>
 *  &emsp; Generates HTTP Responses with all requested info and default fields for API
 */
public interface ServiceInfoFactory {

    /**
     * Default Service Response
     */
    record ServiceInfoResponse(
            String message,
            ZonedDateTime timestamp,
            String applicationName,
            String version
    ) { }

    /**
     * Default Service Response when an error needs to be reported
     */
    record ServiceErrorResponse(
            String error,
            String description,
            ZonedDateTime timestamp,
            String applicationName,
            String version,
            Object additionalInfo
    ) { }

    ResponseEntity<ServiceInfoResponse> getHttpResponseWithServiceInfo(HttpStatus status, String message);

    ResponseEntity<ServiceErrorResponse> getHttpErrorResponse(HttpStatus status, String exceptionName, String description);

    ResponseEntity<ServiceErrorResponse> getHttpErrorResponse(HttpStatus status, String exceptionName, String description, Object additionalInfo);
}
