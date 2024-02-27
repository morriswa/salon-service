package org.morriswa.salon.dao;

import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;

import java.util.List;

public interface EmployeeDao {

    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

    List<ProvidedService> retrieveAllProvidedServices(Long employeeId);

    void deleteProvidedService(Long employeeId, Long serviceId);

    void updateAppointmentDetails(Long userId, Long appointmentId, AppointmentRequest request);

    void cancelAppointment(Long employeeId, Long appointmentId);

    void addContentToProvidedService(Long serviceId, String contentId);

    boolean serviceBelongsTo(Long serviceId, Long userId);

    List<String> retrieveProvidedServiceContent(Long userId, Long serviceId);
}