package org.morriswa.salon.service;

import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface SchedulingService {

    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws Exception;

    void bookAppointment(Long clientId, AppointmentRequest request) throws Exception;

    void employeeCancelsAppointment(Long employeeId, Long appointmentId);

    void clientCancelsAppointment(Long clientId, Long appointmentId);

    void employeeReschedulesAppointment(Long userId, Long appointmentId, AppointmentRequest request) throws Exception;
}
