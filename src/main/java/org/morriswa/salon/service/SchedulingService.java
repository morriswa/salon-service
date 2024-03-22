package org.morriswa.salon.service;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentOpening;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.UserAccount;

import java.time.LocalDate;
import java.util.List;

/**
 * responsible for validating and maintaining the salon's schedule for employees and clients
 *
 * @author William A. Morris
 * @since 2024-02-12
 */
public interface SchedulingService {

// CREATE

    /**
     * books a new appointment
     *
     * @param principal of the authenticated client
     * @param request containing required information about the new appointment
     * @throws Exception if the appointment could not be booked
     */
    void bookAppointment(UserAccount principal, AppointmentRequest request) throws Exception;

// RETRIEVE

    /**
     * @param principal current user
     * @param request containing search params
     * @return all appointment openings on requested day
     * @throws Exception if appointment openings could not be retrieved
     */
    List<AppointmentOpening> retrieveAppointmentOpenings(UserAccount principal, AppointmentRequest request) throws Exception;

    /**
     * @param principal the authenticated client
     * @return all of a client's scheduled appointments
     */
    List<Appointment> retrieveScheduledAppointments(UserAccount principal);

    /**
     * @param principal the authenticated employee
     * @param untilDate the last date to retrieve schedule for
     * @return all employee's schedule appointments within requested timeframe
     */
    List<Appointment> retrieveEmployeeSchedule(UserAccount principal, LocalDate untilDate);

// UPDATE

    /**
     * moves a currently scheduled appointment
     *
     * @param principal the authenticated employee
     * @param appointmentId the appointment to move
     * @param request containing new appointment time
     * @throws Exception if the appointment could not be rescheduled
     */
    void employeeReschedulesAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception;

    /**
     * updates details of a currently scheduled appointment
     *
     * @param principal the authenticated employee
     * @param appointmentId to update
     * @param request new appointment details
     * @throws Exception if appointment could not be updated
     */
    void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception;
}
