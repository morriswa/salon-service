package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.ApplicationUser;
import org.morriswa.eecs447.service.ExampleService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> registerUserAsClient(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @PostMapping("/client/booking")
    public ResponseEntity<?> bookAppointment(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/client/booking")
    public ResponseEntity<?> deleteAppointment(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/client/booking")
    public ResponseEntity<?> retrieveAppointments(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }
}
