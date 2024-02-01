package org.morriswa.eecs447.control;

import org.morriswa.eecs447.model.Appointment;
import org.morriswa.eecs447.model.ProvidedService;
import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.service.EmployeeService;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing employee/business functions that can be consumed by other applications
 */

@RestController
public class EmployeeController {

    private final HttpResponseFactory response;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(HttpResponseFactory response,
                              EmployeeService employeeService) {
        this.response = response;
        this.employeeService = employeeService;
    }

    
    @PostMapping("/employee/services")
    public ResponseEntity<?> addProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ProvidedService createProvidedServiceRequest
    ) {
        employeeService.createProvidedService(principal, createProvidedServiceRequest);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/employee/service/{serviceId}/details")
    public ResponseEntity<?> getProvidedServiceDetails(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        employeeService.getProvidedServiceDetails(principal, serviceId);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @PostMapping("/employee/service/{serviceId}/content")
    public ResponseEntity<?> addPhotosToProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId,
        @RequestPart MultipartFile file
    ) {
        employeeService.uploadProvidedServiceImage(principal, serviceId, file);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/employee/services")
    public ResponseEntity<?> deleteService(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ProvidedService deleteProvidedServiceRequest
    ) {
        employeeService.deleteProvidedService(principal, deleteProvidedServiceRequest);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/employee/schedule")
    public ResponseEntity<?> retrieveEmployeeSchedule(@AuthenticationPrincipal UserAccount principal) {
        var schedule = employeeService.retrieveSchedule(principal);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!", schedule);
    }

    @DeleteMapping("/employee/schedule")
    public ResponseEntity<?> deleteAppointment(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody Appointment deleteRequest
    ) {
        employeeService.cancelAppointment(deleteRequest);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }
}
