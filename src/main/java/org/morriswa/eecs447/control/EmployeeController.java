package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.ApplicationUser;
import org.morriswa.eecs447.service.ExampleService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing employee/business functions that can be consumed by other applications
 */

@RestController
public class EmployeeController {

    private final HttpResponseFactory response;
    private final ExampleService exampleService;
    @Autowired
    public EmployeeController(HttpResponseFactory response,
                              ExampleService exampleService) {
        this.response = response;
        this.exampleService = exampleService;
    }

    @PostMapping("/employee/register")
    public ResponseEntity<?> registerUserAsEmployee(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @PostMapping("/employee/services")
    public ResponseEntity<?> addProvidedService(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/employee/services")
    public ResponseEntity<?> deleteService(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/employee/schedule")
    public ResponseEntity<?> retrieveEmployeeSchedule(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/employee/schedule")
    public ResponseEntity<?> deleteAppointment(@AuthenticationPrincipal ApplicationUser principal) {
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }
}
