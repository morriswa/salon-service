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
 * PURPOSE: Provides an interface to maintain the salon's schedule in the database
 */
public interface ScheduleDao {

    // CREATE
    void bookAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;

    // RETRIEVE
    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws BadRequestException;
    List<Appointment> retrieveScheduledAppointments(Long userId);
    List<Appointment> retrieveEmployeeSchedule(Long employeeId, LocalDate untilDate);
    void checkEditAccessOrThrow(Long employeeId, Long appointmentId) throws BadRequestException;

    // UPDATE
    void updateAppointmentDetails(Long appointmentId, AppointmentRequest request);
    void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws BadRequestException;
}
