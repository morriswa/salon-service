package org.morriswa.salon.control;

import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.EmployeeService;
import org.morriswa.salon.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

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

    
    @PostMapping("/employee/service")
    public ResponseEntity<?> addProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ProvidedService createProvidedServiceRequest
    ) throws Exception {
        employeeService.createProvidedService(principal, createProvidedServiceRequest);
        return response.build(HttpStatus.OK,"This endpoint is still in development!");
    }

    @GetMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> getProvidedServiceDetails(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        employeeService.getProvidedServiceDetails(principal, serviceId);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @PostMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> addPhotosToProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId,
        @RequestPart MultipartFile file
    ) {
        employeeService.uploadProvidedServiceImage(principal, serviceId, file);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @DeleteMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> deleteService(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        employeeService.deleteProvidedService(principal, serviceId);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }

    @GetMapping("/employee/schedule")
    public ResponseEntity<?> retrieveEmployeeSchedule(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam Optional<LocalDateTime> untilDate
    ) {
        final var schedule = employeeService.retrieveSchedule(principal,
            // if the user doesn't provide a date, only get the schedule for the next 2 weeks
            untilDate.orElse(LocalDateTime.now().plusWeeks(2L)));
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!", schedule);
    }

    @DeleteMapping("/employee/schedule/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long appointmentId
    ) {
        employeeService.cancelAppointment(principal, appointmentId);
        return response.build(HttpStatus.NOT_IMPLEMENTED,"This endpoint is still in development!");
    }
}
