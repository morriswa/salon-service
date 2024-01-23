package org.morriswa.eecs447.control;

import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; provides RESTful endpoints to ensure application is running
 */

@RestController //Indicates that the class is code for a REST controller ( Provides endpoints to user )
public class HealthController {

    private final HttpResponseFactory response;

    @Autowired // Automatically injects dependencies into the constructor as params
    public HealthController(HttpResponseFactory response) {
        // assign dependencies to class attributes
        this.response = response;
    }

    @GetMapping("/health") //Maps and endpoint to a method, GET method in postman
    public ResponseEntity<?> getServiceHealth() {
        //Returns formatted JSON response
        return response.build(HttpStatus.OK, "All is good on our end!");
    }
}
