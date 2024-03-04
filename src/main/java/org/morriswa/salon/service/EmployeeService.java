package org.morriswa.salon.service;

import java.time.LocalDate;
import java.util.List;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.*;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    
    void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception;

    ServiceDetailsResponse getProvidedServiceDetails(UserAccount principal, Long serviceId) throws Exception;

    List<ProvidedService> retrieveAllProvidedServices(UserAccount principal);

    void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile image) throws Exception;

    void deleteProvidedService(UserAccount principal, Long serviceId);

    List<Appointment> retrieveSchedule(UserAccount principal, LocalDate untilDate);

    void rescheduleAppointment(UserAccount principal, Long appointmentId, AppointmentRequest request) throws Exception;

    void updateAppointmentDetails(UserAccount principal, Long appointmentId, AppointmentRequest request);

    void cancelAppointment(UserAccount principal, Long appointmentId);



}
