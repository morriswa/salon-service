package org.morriswa.salon.control;

import org.morriswa.salon.model.*;
import org.morriswa.salon.service.ProfileService;
import org.morriswa.salon.service.ProvidedServiceService;
import org.morriswa.salon.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * provides a REST API for performing employee/business functions that can be consumed by other applications
 * @author William A. Morris
 * @since 2024-01-25
 */

@RestController @RequestMapping("/employee")
public class EmployeeController {

    private final SchedulingService schedule;
    private final ProvidedServiceService providedServices;
    private final ProfileService profileService;

    @Autowired
    public EmployeeController(SchedulingService schedule, ProvidedServiceService providedServices, ProfileService profileService) {
        this.schedule = schedule;
        this.providedServices = providedServices;
        this.profileService = profileService;
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal currently authenticated User Account
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @GetMapping("/profile")
    public ResponseEntity<EmployeeInfo> getEmployeeProfile(
            @AuthenticationPrincipal UserAccount principal
    ) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = profileService.getEmployeeProfile(principal);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal currently authenticated User Account
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @PostMapping("/profile/image")
    public ResponseEntity<Void> updateProfileImage(
            @AuthenticationPrincipal UserAccount principal,
            @RequestPart MultipartFile image
    ) throws Exception{
        // using the user profile service, retrieve the current users profile
        profileService.updateEmployeeProfileImage(principal, image);
        // and return it to them in JSON format
        return ResponseEntity.noContent().build();
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal currently authenticated User Account
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateEmployeeProfile(
            @AuthenticationPrincipal UserAccount principal,
            @RequestBody EmployeeInfo request
    ) throws Exception{
        // using the user profile service, retrieve the current users profile
        profileService.updateEmployeeProfile(principal, request);
        // and return it to them in JSON format
        return ResponseEntity.noContent().build();
    }


    /**
     * HTTP Get endpoint for employees to view all the services they are providing to users
     *
     * @param principal currently authenticated employee
     * @return an array of provided services
     */
    @GetMapping("/services")
    public ResponseEntity<List<ProvidedService>> retrieveAllProvidedServices(@AuthenticationPrincipal UserAccount principal) {
        var services = providedServices.retrieveEmployeesServices(principal.getUserId());
        return ResponseEntity.ok(services);
    }

    /**
     * HTTP Patch endpoint to update services
     *
     * @param principal authenticated employee
     * @param request update params
     * @param serviceId to update
     * @throws Exception if the service could not be updated
     */
    @PatchMapping("/service/{serviceId}")
    public ResponseEntity<Void> updateProvidedServiceDetails(
            @AuthenticationPrincipal UserAccount principal,
            @RequestBody ProvidedService request,
            @PathVariable Long serviceId
    ) throws Exception {
        providedServices.updateProvidedServiceDetails(principal, serviceId, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Post endpoint for employees to add their services to the client portal for booking
     *
     * @param principal currently authenticated employee
     * @param createProvidedServiceRequest with info required to create a provided service
     * @return no content
     * @throws Exception return error response if the service could not be registered
     */
    @PostMapping("/service")
    public ResponseEntity<Void> createProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @RequestBody ProvidedService createProvidedServiceRequest
    ) throws Exception {
        providedServices.createProvidedService(principal, createProvidedServiceRequest);
        return ResponseEntity.noContent().build();
    }


    /**
     * HTTP Post endpoint to add new content to a provided service's page
     *
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @param image an image to add to service's profile
     * @return no content
     * @throws Exception return error response if the image could not be stored
     */
    @PostMapping("/service/{serviceId}")
    public ResponseEntity<Void> uploadProvidedServiceImage(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId,
        @RequestPart MultipartFile image
    ) throws Exception {
        providedServices.uploadProvidedServiceImage(principal, serviceId, image);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @return no content
     */
    @GetMapping("/service/{serviceId}/images")
    public ResponseEntity<Map<String, URL>> getProvidedServiceImages(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long serviceId
    ) {
        var images = providedServices.getProvidedServiceImages(principal, serviceId);
        return ResponseEntity.ok(images);
    }

    /**
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @return no content
     * @throws Exception return error response if the image could not be stored
     */
    @DeleteMapping("/service/{serviceId}/image/{contentId}")
    public ResponseEntity<Map<String, URL>> deleteProvidedServiceImage(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long serviceId,
            @PathVariable String contentId
    ) throws Exception {
        providedServices.deleteProvidedServiceImage(principal, serviceId, contentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Delete endpoint to remove a provided service from client portal
     * This service will still exist within the table, but will not be available for booking
     *
     * @param principal currently authenticated employee
     * @param serviceId associated with the service to modify
     * @return no content
     */
    @DeleteMapping("/service/{serviceId}")
    public ResponseEntity<?> deleteProvidedService(
        @AuthenticationPrincipal UserAccount principal,
        @PathVariable Long serviceId
    ) {
        providedServices.deleteProvidedService(principal, serviceId);
        return ResponseEntity.noContent().build();
    }

    /**
     * HTTP Get endpoint to retrieve an employee's currently scheduled appointments
     *
     * @param principal currently authenticated employee
     * @param untilDate last date to retrieve appointments for
     * @return all scheduled appointments if successful, else error response
     */
    @GetMapping("/schedule")
    public ResponseEntity<List<Appointment>> retrieveSchedule(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam Optional<LocalDate> untilDate
    ) {
        final var retrieved = schedule.retrieveEmployeeSchedule(principal,
            // if the user doesn't provide a date, only get the schedule for the next 2 weeks
            untilDate.orElse(LocalDate.now().plusWeeks(2L)));
        return ResponseEntity.ok(retrieved);
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
    @PatchMapping("/appointment/{appointmentId}")
    public ResponseEntity<Void> updateAppointmentDetails(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long appointmentId,
            @RequestBody AppointmentRequest request
    ) throws Exception {
        schedule.updateAppointmentDetails(principal, appointmentId, request);
        return ResponseEntity.noContent().build();
    }
}
