package org.morriswa.salon.control;

import org.morriswa.salon.exception.BadRequestException;
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


    @GetMapping("/client/book")
    public ResponseEntity<?> seeAvailableAppointmentTimes(
            @AuthenticationPrincipal UserAccount principal, @RequestBody AppointmentRequest request
    ) throws BadRequestException {
        var book = clientService.seeTimes(principal, request);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/client/booking")
    public ResponseEntity<?> bookAppointment(
            @AuthenticationPrincipal UserAccount principal, @RequestBody AppointmentRequest request
    ) throws BadRequestException {
        clientService.requestAppointment(principal, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/client/booking/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
            @AuthenticationPrincipal UserAccount principal, @PathVariable Long appointmentId
    ) {
        clientService.cancelAppointment(principal, appointmentId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/client/booking")
    public ResponseEntity<?> retrieveScheduledAppointments(@AuthenticationPrincipal UserAccount principal) {
        clientService.retrieveScheduledAppointments(principal);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/client/billing")
    public ResponseEntity<?> retrieveUnpaidAppointments(@AuthenticationPrincipal UserAccount principal) {
        clientService.retrieveUnpaidAppointments(principal);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
