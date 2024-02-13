package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;

import java.util.List;

public interface ScheduleDao {
    List<AppointmentLength> checkAvailableTimes(AppointmentRequest request) throws BadRequestException;

    void registerAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;
}
