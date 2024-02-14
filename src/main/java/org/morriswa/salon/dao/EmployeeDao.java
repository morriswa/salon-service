package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeDao {

    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

    void deleteProvidedService(Long employeeId, Long serviceId);

    List<Appointment> retrieveSchedule(Long employeeId, LocalDateTime untilDate);

    void cancelAppointment(Long employeeId, Long appointmentId);

    List<ProvidedService> retrieveAllProvidedServices(Long employeeId);

    void updateAppointmentDetails(Long userId, Long appointmentId, AppointmentRequest request);
}