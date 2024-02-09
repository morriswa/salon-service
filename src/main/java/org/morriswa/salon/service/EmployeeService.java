package org.morriswa.salon.service;

import java.time.LocalDateTime;
import java.util.List;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    
    void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception;

    void deleteProvidedService(UserAccount principal, Long serviceId);

    List<Appointment> retrieveSchedule(UserAccount principal, LocalDateTime untilDate);

    void cancelAppointment(UserAccount principal, Long appointmentId);

    void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile file);

    void getProvidedServiceDetails(UserAccount principal, Long serviceId);

    List<ProvidedService> retrieveAllProvidedServices(UserAccount principal);
}
