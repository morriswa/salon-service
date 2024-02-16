package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;

import java.util.List;

public interface ClientService {
    void bookAppointment(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;

    void cancelAppointment(UserAccount principal, Long appointmentId);

    List<Appointment> retrieveScheduledAppointments(UserAccount principal);

    List<Appointment> retrieveUnpaidAppointments(UserAccount principal);

    List<AppointmentOpening> retrieveAppointmentOpenings(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;
}
