package org.morriswa.eecs447.service;

import java.util.List;

import org.morriswa.eecs447.model.Appointment;
import org.morriswa.eecs447.model.ProvidedService;
import org.morriswa.eecs447.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    
    void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest);

    void deleteProvidedService(UserAccount principal, ProvidedService deleteProvidedServiceRequest);

    List<Appointment> retrieveSchedule(UserAccount principal);

    void cancelAppointment(Appointment deleteRequest);

    void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile file);

    void getProvidedServiceDetails(UserAccount principal, Long serviceId);
}
