package org.morriswa.salon.dao;

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

    /**
     * store a new appointment in mysql
     *
     * @param clientId booking the appointment
     * @param request required details to book appointment
     * @throws Exception if appointment could not be booked
     */
    void bookAppointment(Long clientId, AppointmentRequest request) throws Exception;

// RETRIEVE

    /**
     * @param clientId requester
     * @param request containing search params
     * @return list of all appointment openings on a given day
     * @throws Exception if appointment openings could not be retrieved
     */
    List<AppointmentOpening> retrieveAppointmentOpenings(Long clientId, AppointmentRequest request) throws Exception;

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

    /**
     * @param employeeId attempting to edit an existing appointment
     * @param appointmentId to edit
     * @throws Exception if employee cannot edit appointment
     */
    void checkEditAccessOrThrow(Long employeeId, Long appointmentId) throws Exception;

// UPDATE

    /**
     * updates details of an appointment (other than date/time)
     *
     * @param appointmentId to edit
     * @param request details to update
     */
    void updateAppointmentDetails(Long appointmentId, AppointmentRequest request);

    /**
     * reschedule an existing appointment
     * @param employeeId rescheduling appointment
     * @param appointmentId to reschedule
     * @param request containing params
     * @throws Exception if appointment could not be rescheduled
     */
    void employeeReschedulesAppointment(Long employeeId, Long appointmentId, AppointmentRequest request) throws Exception;
}
