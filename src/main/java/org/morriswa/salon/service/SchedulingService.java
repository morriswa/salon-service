package org.morriswa.salon.service;

import org.morriswa.salon.exception.BadRequestException;
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
    void bookAppointment(UserAccount principal, AppointmentRequest request) throws Exception;

    // RETRIEVE
    List<AppointmentOpening> retrieveAppointmentOpenings(AppointmentRequest request) throws Exception;
    List<Appointment> retrieveScheduledAppointments(UserAccount principal);
    List<Appointment> retrieveEmployeeSchedule(UserAccount principal, LocalDate untilDate);

    // UPDATE
    void employeeReschedulesAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception;
    void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request) throws BadRequestException;

    // DELETE
    void employeeCancelsAppointment(Long employeeId, Long appointmentId);
    void clientCancelsAppointment(Long clientId, Long appointmentId);

}
