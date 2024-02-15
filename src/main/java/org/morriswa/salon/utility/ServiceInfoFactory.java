package org.morriswa.salon.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *  AUTHOR: William A. Morris <br>
 *  CREATION_DATE: 2024-01-19 <br>
 *  PURPOSE: <br>
 *  &emsp; Generates HTTP Responses with all requested info and default fields for API
 */
public interface ServiceInfoFactory {

    ResponseEntity<?> getHttpResponseWithServiceInfo(HttpStatus status, String message);

    ResponseEntity<?> getHttpErrorResponse(HttpStatus status, String exceptionName, String description);

    ResponseEntity<?> getHttpErrorResponse(HttpStatus status, String exceptionName, String description, Object additionalInfo);
}