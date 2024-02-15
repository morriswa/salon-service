package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeDao {

    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

    List<ProvidedService> retrieveAllProvidedServices(Long employeeId);

    void deleteProvidedService(Long employeeId, Long serviceId);

    List<Appointment> retrieveSchedule(Long employeeId, LocalDate untilDate);

    void updateAppointmentDetails(Long userId, Long appointmentId, AppointmentRequest request);

    void cancelAppointment(Long employeeId, Long appointmentId);
}