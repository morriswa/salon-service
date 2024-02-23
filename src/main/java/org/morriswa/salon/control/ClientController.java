package org.morriswa.salon.control;

import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing client functions that can be consumed by other applications
 */

@RestController
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    /**
     * HTTP Get endpoint to see available appointment times for a given day
     *
     * @param principal currently authenticated client
     * @param request search parameters
     * @return all appointment openings
     * @throws Exception return error response if appointment openings could not be retrieved
     */
    @PostMapping("/client/booking")
    public ResponseEntity<?> retrieveAppointmentOpenings(
            @AuthenticationPrincipal UserAccount principal, @RequestBody AppointmentRequest request
    ) throws Exception {
        var book = clientService.retrieveAppointmentOpenings(principal, request);
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
    @PostMapping("/client/book")
    public ResponseEntity<?> bookAppointment(
            @AuthenticationPrincipal UserAccount principal, @RequestBody AppointmentRequest request
    ) throws Exception {
        clientService.bookAppointment(principal, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/client/booked/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @AuthenticationPrincipal UserAccount principal, @PathVariable Long appointmentId
    ) {
//        clientService.cancelAppointment(principal, appointmentId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/client/booked")
    public ResponseEntity<?> retrieveScheduledAppointments(@AuthenticationPrincipal UserAccount principal) {
        final var appointments = clientService.retrieveScheduledAppointments(principal);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/client/billing")
    public ResponseEntity<?> retrieveUnpaidAppointments(@AuthenticationPrincipal UserAccount principal) {
//        final var appointments = clientService.retrieveUnpaidAppointments(principal);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/client/services")
    public ResponseEntity<?> searchAvailableService(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam String searchText
    ) throws Exception {
        final var services = clientService.searchAvailableService(principal, searchText);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/client/service/{serviceId}")
    public ResponseEntity<?> retrieveServiceDetails(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable Long serviceId
    ) throws Exception {
        final var service = clientService.retrieveServiceDetails(principal, serviceId);
        return ResponseEntity.ok(service);
    }
}
