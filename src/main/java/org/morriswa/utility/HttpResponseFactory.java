package org.morriswa.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *  AUTHOR: William A. Morris <br>
 *  CREATION_DATE: 2024-01-19 <br>
 *  PURPOSE: <br>
 *  &emsp; Generates HTTP Responses with all requested info and default fields for API
 */
public interface HttpResponseFactory {

    ResponseEntity<?> build(HttpStatus status, String message);

    ResponseEntity<?> build(HttpStatus status, String message, Object payload);

    ResponseEntity<?> error(HttpStatus status, String message, String description);

    ResponseEntity<?> error(HttpStatus status, String message, String description, Object stack);
}
