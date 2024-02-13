package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface SchedulingService {

    List<AppointmentLength> seeAvailableTimes(Long clientId, AppointmentRequest request) throws BadRequestException;

    void scheduleAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;

    void employeeCancelsAppointment(Long employeeId, Long appointmentId);

    void clientCancelsAppointment(Long clientId, Long appointmentId);
}
