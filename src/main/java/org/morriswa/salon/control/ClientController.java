package org.morriswa.salon.control;

import org.morriswa.salon.model.*;
import org.morriswa.salon.service.ProfileService;
import org.morriswa.salon.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * provides a REST API for performing client functions that can be consumed by other applications
 *
 * @author William A. Morris
 * @since 2024-01-25
 */

@RestController @RequestMapping("/client")
public class ClientController {

    private final ProfileService profiles;
    private final SchedulingService schedule;

    @Autowired
    public ClientController(ProfileService profiles, SchedulingService schedule) {
        this.profiles = profiles;
        this.schedule = schedule;
    }


    /**
     * HTTP Get endpoint to see available appointment times for a given day
     *
     * @param request search parameters
     * @return all appointment openings
     * @throws Exception return error response if appointment openings could not be retrieved
     */
    @PostMapping("/schedule")
    public ResponseEntity<List<AppointmentOpening>> retrieveAppointmentOpenings(
            @AuthenticationPrincipal UserAccount principal,
            @RequestBody AppointmentRequest request
    ) throws Exception {
        var book = schedule.retrieveAppointmentOpenings(principal, request);
        return ResponseEntity.ok(book);
    }

    /**
     * Http Post endpoint to book an appointment
     *
     * @param principal currently authenticated client
     * @param request all info required to book appointment
     * @return no content
     * @throws Exception return error response if appointment was not booked
     */
    @PostMapping("/schedule/confirm")
    public ResponseEntity<Void> bookAppointment(
            @AuthenticationPrincipal UserAccount principal, @RequestBody AppointmentRequest request
    ) throws Exception {
        schedule.bookAppointment(principal, request);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/schedule")
    public ResponseEntity<List<Appointment>> retrieveScheduledAppointments(@AuthenticationPrincipal UserAccount principal) {
        final var appointments = schedule.retrieveScheduledAppointments(principal);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @param principal currently authenticated User Account
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @GetMapping("/profile")
    public ResponseEntity<ClientInfo> getClientProfile(@AuthenticationPrincipal UserAccount principal) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = profiles.getClientProfile(principal);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
    }

    /**
     * HTTP Patch endpoint to update an existing user's contact info
     *
     * @param principal authenticated user
     * @param updateProfileRequest containing contact info to be updated
     * @return blank response
     * @throws Exception return error response if user's profile cannot be updated
     */
    @PatchMapping("/profile")
    public ResponseEntity<Void> updateClientProfile(
            @AuthenticationPrincipal UserAccount principal,
            @RequestBody ClientInfo updateProfileRequest
    ) throws Exception {
        profiles.updateClientProfile(principal, updateProfileRequest);
        return ResponseEntity.noContent().build();
    }


}
