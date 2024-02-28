package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-12
 * PURPOSE: Provides a Java interface to maintain the salon's schedule in the database
 */
public interface ScheduleDao {
    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws BadRequestException;

    void bookAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;

    void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws BadRequestException;

    List<Appointment> retrieveEmployeeSchedule(Long employeeId, LocalDate untilDate);

}
