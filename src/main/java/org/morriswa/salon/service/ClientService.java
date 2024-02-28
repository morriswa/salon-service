package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.*;

import java.util.List;

public interface ClientService {
    void bookAppointment(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;

    void cancelAppointment(UserAccount principal, Long appointmentId);

    List<Appointment> retrieveScheduledAppointments(UserAccount principal);

    List<Appointment> retrieveUnpaidAppointments(UserAccount principal);

    List<AppointmentOpening> retrieveAppointmentOpenings(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;

    List<ServiceDetails> searchAvailableService(UserAccount principal, String searchText);

    ServiceDetails retrieveServiceDetails(UserAccount principal, Long serviceId) throws BadRequestException;
}
