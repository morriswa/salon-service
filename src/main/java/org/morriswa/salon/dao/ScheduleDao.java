package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface ScheduleDao {
    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws BadRequestException;

    void bookAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;

    void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws BadRequestException;
}
