package org.morriswa.salon.service;

import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.model.ProvidedServiceProfile;
import org.morriswa.salon.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProvidedServiceService {

    // CREATE
    void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception;

    // RETRIEVE
    List<ProvidedService> retrieveEmployeesServices(Long employeeId);
    ProvidedServiceProfile retrieveServiceProfile(Long serviceId) throws Exception;
    List<ProvidedServiceDetails> searchAvailableService(String searchText);

    // UPDATE
    void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile image) throws Exception;

    // DELETE
    void deleteProvidedService(UserAccount principal, Long serviceId);

}
