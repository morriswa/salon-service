package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;

import java.util.List;

public interface ClientService {
    void requestAppointment(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;

    void cancelAppointment(UserAccount principal, Long appointmentId);

    void retrieveScheduledAppointments(UserAccount principal);

    void retrieveUnpaidAppointments(UserAccount principal);

    List<AppointmentLength> seeTimes(UserAccount principal, AppointmentRequest request) throws BadRequestException, Exception;
}
