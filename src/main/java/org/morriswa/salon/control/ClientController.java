package org.morriswa.salon.control;

import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.ExampleService;
import org.morriswa.salon.utility.HttpResponseFactory;
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

    private final ExampleService exampleService;

    @Autowired
    public ClientController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @PostMapping("/client/register")
    public ResponseEntity<?> registerUserAsClient(@AuthenticationPrincipal UserAccount principal) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/client/booking")
    public ResponseEntity<?> bookAppointment(@AuthenticationPrincipal UserAccount principal) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/client/booking")
    public ResponseEntity<?> deleteAppointment(@AuthenticationPrincipal UserAccount principal) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/client/booking")
    public ResponseEntity<?> retrieveAppointments(@AuthenticationPrincipal UserAccount principal) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
