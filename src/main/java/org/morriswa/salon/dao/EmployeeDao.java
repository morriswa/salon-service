package org.morriswa.salon.dao;

import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ServiceDetails;
import org.morriswa.salon.model.ProvidedService;

import java.util.List;

/**
 * AUTHOR: William A. Morris
 * DATE CREATED: 2024-02-01
 * PURPOSE: Provides a Java interface to maintain employee information in database
 */
public interface EmployeeDao {

    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

    List<ProvidedService> retrieveAllProvidedServices(Long employeeId);

    void deleteProvidedService(Long employeeId, Long serviceId);

    void updateAppointmentDetails(Long employeeId, Long appointmentId, AppointmentRequest request);

    void cancelAppointment(Long employeeId, Long appointmentId);

    void addContentToProvidedService(Long serviceId, String contentId);

    boolean serviceBelongsTo(Long serviceId, Long employeeId);

    List<String> retrieveProvidedServiceContent(Long employeeId, Long serviceId);

    ServiceDetails retrieveProvidedServiceDetails(Long serviceId) throws Exception;
}