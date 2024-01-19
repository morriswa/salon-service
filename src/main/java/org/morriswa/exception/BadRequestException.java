package org.morriswa.exception;

/**
 * Generic Exception to throw when the server cannot complete a request for an expected reason
 */
public class BadRequestException extends Exception {
    public BadRequestException(String msg) {
        super(msg);
    }
}