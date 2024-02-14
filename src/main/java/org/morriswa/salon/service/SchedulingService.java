package org.morriswa.salon.service;

import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.EditAppointmentRequest;

import java.util.List;

public interface SchedulingService {

    List<AppointmentLength> seeAvailableTimes(AppointmentRequest request) throws Exception;

    void scheduleAppointment(Long clientId, AppointmentRequest request) throws Exception;

    void employeeCancelsAppointment(Long employeeId, Long appointmentId);

    void clientCancelsAppointment(Long clientId, Long appointmentId);

    void employeeReschedulesAppointment(Long userId, Long appointmentId, EditAppointmentRequest request) throws Exception;
}
