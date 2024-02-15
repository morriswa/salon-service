package org.morriswa.salon.control;

import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing employee/business functions that can be consumed by other applications
 */

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    /**
     * HTTP Post endpoint for employees to add their services to the client portal for booking
     *
     * @param principal currently authenticated employee
     * @param createProvidedServiceRequest with info required to create a provided service
     * @return no content
     * @throws Exception return error response if the service could not be registered
     */
    @PostMapping("/employee/service")
    public ResponseEntity<?> createProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ProvidedService createProvidedServiceRequest
    ) throws Exception {
        employeeService.createProvidedService(principal, createProvidedServiceRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Get endpoint for employees to view all the services they are providing to users
     *
     * @param principal currently authenticated employee
     * @return an array of provided services
     * @throws Exception return error response if employee's services could not be retrieved
     */
    @GetMapping("/employee/service")
    public ResponseEntity<?> retrieveAllProvidedServices(@AuthenticationPrincipal UserAccount principal) throws Exception {
        var services = employeeService.retrieveAllProvidedServices(principal);
        return ResponseEntity.ok(services);
    }

    /**
     * Http GET endpoint for employees to retrieve all stored information about a provided service
     *
     * @param principal currently authenticated employee
     * @param serviceId associated with the service
     * @return all information about requested service
     */
    @GetMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> getProvidedServiceDetails(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        employeeService.getProvidedServiceDetails(principal, serviceId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * HTTP Post endpoint to add new content to a provided service's page
     *
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @param file an image to add to service's profile
     * @return no content
     * @throws Exception return error response if the image could not be stored
     */
    @PostMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> uploadProvidedServiceImage(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId,
        @RequestPart MultipartFile file
    ) throws Exception {
        employeeService.uploadProvidedServiceImage(principal, serviceId, file);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * HTTP Delete endpoint to remove a provided service from client portal
     * This service will still exist within the table, but will not be available for booking
     *
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @return no content
     */
    @DeleteMapping("/employee/service/{serviceId}")
    public ResponseEntity<?> deleteProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        employeeService.deleteProvidedService(principal, serviceId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * HTTP Get endpoint to retrieve an employee's currently scheduled appointments
     *
     * @param principal currently authenticated employee
     * @param untilDate last date to retrieve appointments for
     * @return all scheduled appointments if successful, else error response
     */
    @GetMapping("/employee/schedule")
    public ResponseEntity<?> retrieveSchedule(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam Optional<LocalDate> untilDate
    ) {
        final var schedule = employeeService.retrieveSchedule(principal,
            // if the user doesn't provide a date, only get the schedule for the next 2 weeks
            untilDate.orElse(LocalDate.now().plusWeeks(2L)));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * HTTP Patch endpoint to move a currently scheduled appointment
     *
     * @param principal currently authenticated employee
     * @param appointmentId associated with the appointment to reschedule
     * @param request containing required information to move the apt.
     * @return no content
     * @throws Exception return error response if the appointment could not be rescheduled
     */
    @PatchMapping("/employee/schedule/{appointmentId}")
    public ResponseEntity<?> rescheduleAppointment(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequest request
    ) throws Exception {
        employeeService.rescheduleAppointment(principal, appointmentId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * HTTP Patch endpoint to modify details of a scheduled appointment
     *
     * @param principal currently authenticated employee
     * @param appointmentId associated with the appointment to modify
     * @param request information to update
     * @return no content
     * @throws Exception return error response if the appointment could not be modified
     */
    @PatchMapping("/employee/appointment/{appointmentId}")
    public ResponseEntity<?> updateAppointmentDetails(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequest request
            ) throws Exception {
        employeeService.updateAppointmentDetails(principal, appointmentId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * HTTP Delete endpoint used to cancel a scheduled appointment
     * This appointment will still exist within the table, but will not be billed
     *
     * @param principal currently authenticated employee
     * @param appointmentId associated with the appointment to cancel
     * @return no content
     * @throws Exception return error response if the appointment could not be canceled
     */
    @DeleteMapping("/employee/appointment/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long appointmentId
    ) throws Exception {
        employeeService.cancelAppointment(principal, appointmentId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
