package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;

import java.util.List;

public interface ProvidedServiceDao {

    // CREATE
    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

    // RETRIEVE
    List<ProvidedServiceDetails> searchAvailableServices(String searchText);
    List<ProvidedService> retrieveEmployeesServices(Long employeeId);
    ProvidedServiceDetails retrieveServiceDetails(Long serviceId) throws BadRequestException;
    List<String> retrieveServiceContent(Long serviceId);
    boolean serviceBelongsTo(Long serviceId, Long employeeId);

    // UPDATE
    void addContentToProvidedService(Long serviceId, String contentId);

    // DELETE
    void deleteProvidedService(Long employeeId, Long serviceId);
}
