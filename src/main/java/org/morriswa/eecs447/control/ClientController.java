package org.morriswa.eecs447.control;

import org.morriswa.eecs447.service.ExampleService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing client functions that can be consumed by other applications
 */

@RestController
public class ClientController {

    private final HttpResponseFactory response;
    private final ExampleService exampleService;
    @Autowired
    public ClientController(HttpResponseFactory response,
                            ExampleService exampleService) {
        this.response = response;
        this.exampleService = exampleService;
    }

    @PostMapping("/client/register")
    public ResponseEntity<?> registerUserAsClient(Principal principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @PostMapping("/client/booking")
    public ResponseEntity<?> bookAppointment(Principal principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/client/booking")
    public ResponseEntity<?> deleteAppointment(Principal principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/client/booking")
    public ResponseEntity<?> retrieveAppointments(Principal principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }
}
