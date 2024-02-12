package org.morriswa.salon.service;

import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface SchedulingService {

    List<AppointmentLength> seeAvailableTimes(Long clientId, AppointmentRequest request);

    void scheduleAppointment(Long clientId, AppointmentRequest request);

    void employeeCancelsAppointment(Long employeeId, Long appointmentId);

    void clientCancelsAppointment(Long clientId, Long appointmentId);
}
