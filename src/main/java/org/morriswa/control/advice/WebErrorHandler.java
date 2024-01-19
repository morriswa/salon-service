package org.morriswa.control.advice;

import org.morriswa.exception.BadRequestException;
import org.morriswa.utility.HttpResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; provides error handlers for all controllers
 */
@ControllerAdvice
public class WebErrorHandler {
    @Autowired private Environment env;
    @Autowired private HttpResponseFactory responseFactory;
    private final Logger log = LoggerFactory.getLogger(WebErrorHandler.class);

    @ExceptionHandler({Exception.class}) // Catch any and all exceptions thrown in any controller
    public ResponseEntity<?> internalServerError(Exception e, WebRequest r) {
        log.error("Encountered 500 error: ", e);

        // and return a 500 with as much relevant information as they deserve
        return responseFactory.error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getClass().getSimpleName(),
            e.getMessage());
    }

    @ExceptionHandler({ // catch...
        BadRequestException.class, // Bad Requests,
    })
    public ResponseEntity<?> badRequest(Exception e, WebRequest r) {
        // and assume user fault [400]
        return responseFactory.error(
            HttpStatus.BAD_REQUEST,
            e.getClass().getSimpleName(),
            e.getMessage());
    }

}
