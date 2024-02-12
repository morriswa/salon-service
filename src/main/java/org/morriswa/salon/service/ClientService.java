package org.morriswa.salon.service;

import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;

public interface ClientService {
    void requestAppointment(UserAccount principal, AppointmentRequest request);

    void cancelAppointment(UserAccount principal, Long appointmentId);

    void retrieveScheduledAppointments(UserAccount principal);

    void retrieveUnpaidAppointments(UserAccount principal);
}
