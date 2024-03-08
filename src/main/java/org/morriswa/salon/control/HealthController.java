package org.morriswa.salon.control;

import org.morriswa.salon.utility.ServiceInfoFactory;
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

@RestController
public class HealthController {

    private final ServiceInfoFactory response;

    @Autowired
    public HealthController(ServiceInfoFactory response) {
        this.response = response;
    }

    /**
     * Public HTTP Get method used by deployment service (AWS Lightsail) to ensure application is running and healthy
     * @return important information about the currently running application
     */
    @GetMapping("/health")
    public ResponseEntity<ServiceInfoFactory.ServiceInfoResponse> getServiceHealth() {
        return response.getHttpResponseWithServiceInfo(HttpStatus.OK, "All is good on our end!");
    }
}
