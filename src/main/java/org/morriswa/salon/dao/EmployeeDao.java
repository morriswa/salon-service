package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.ProvidedService;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeDao {

    void createProvidedService(Long userId, ProvidedService createProvidedServiceRequest);

    void deleteProvidedService(Long userId, Long serviceId);

    List<Appointment> retrieveSchedule(Long userId, LocalDateTime untilDate);

    void cancelAppointment(Long userId, Long appointmentId);
} 