package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides an interface to maintain the salon's schedule in the database
 *
 * @author William A. Morris
 * @since 2024-02-12
 */
public interface ScheduleDao {

    // CREATE
    void bookAppointment(Long clientId, AppointmentRequest request) throws BadRequestException;

    // RETRIEVE
    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws BadRequestException;

    /**
     * @param clientId to retrieve schedule for
     * @return all scheduled appointments
     *
     * @author Kevin Rivers
     */
    List<Appointment> retrieveScheduledAppointments(Long clientId);

    /**
     * retrieves an employees schedule from current time until specified date at end of day
     *
     * @param employeeId to retrieve schedule for
     * @param untilDate last date to retrieve appointments for
     * @return all requested appointments, ordered by time
     *
     * @author Makenna Loewenherz
     */
    List<Appointment> retrieveEmployeeSchedule(Long employeeId, LocalDate untilDate);
    void checkEditAccessOrThrow(Long employeeId, Long appointmentId) throws BadRequestException;

    // UPDATE
    void updateAppointmentDetails(Long appointmentId, AppointmentRequest request);
    void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws BadRequestException;
}
