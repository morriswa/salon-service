package org.morriswa.eecs447.control.advice;

import jakarta.servlet.ServletException;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.exception.ValidationException;
import org.morriswa.eecs447.utility.HttpResponseFactory;
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
    private final Environment env;
    private final HttpResponseFactory responseFactory;
    private final Logger log;

    @Autowired public WebErrorHandler(Environment env, HttpResponseFactory responseFactory) {
        this.env = env;
        this.responseFactory = responseFactory;
        this.log = LoggerFactory.getLogger(WebErrorHandler.class);
    }


    // Generic Internal Server Error handler
    @ExceptionHandler({Exception.class}) // Catch any and all unexpected exceptions thrown in any controller
    public ResponseEntity<?> internalServerError(Exception e, WebRequest r) {
        log.error("Encountered unexpected (500) error: ", e);

        // and return a 500 with as much relevant information as they deserve
        return responseFactory.error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getClass().getSimpleName(),
            e.getMessage());
    }

    @ExceptionHandler({ // catches expected exceptions including...
        BadRequestException.class, // Bad Requests...
    })
    public ResponseEntity<?> badRequest(Exception e, WebRequest r) {
        // and assume user fault [400]
        return responseFactory.error(
            HttpStatus.BAD_REQUEST,
            e.getClass().getSimpleName(),
            e.getMessage());
    }

    @ExceptionHandler(ValidationException.class) // exception handler for expected validation errors
    public ResponseEntity<?> validationErrors(Exception e, WebRequest r) {
        ValidationException ve = (ValidationException) e;

        // and assume user fault [400]
        return responseFactory.error(
                HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                "A validation error has occurred!!!",
                ve.getValidationErrors());
    }

    // catches servlet exceptions
    @ExceptionHandler(ServletException.class) // usually this means the user is doing something
    // forbidden by the application
    public ResponseEntity<?> forbiddenRequests(Exception e, WebRequest r) {
        // so return forbidden response 403
        return responseFactory.error(
                HttpStatus.FORBIDDEN,
                e.getClass().getSimpleName(),
                e.getMessage());
    }

}
