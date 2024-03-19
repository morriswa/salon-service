package org.morriswa.salon.service;

import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.model.ProvidedServiceProfile;
import org.morriswa.salon.model.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * responsible for validating and maintaining all provided services
 *
 * @author William A. Morris
 * @since 2024-03-05
 */
public interface ProvidedServiceService {
// CREATE

    /**
     * creates a new service that can be booked by clients
     *
     * @param principal the authenticated employee
     * @param createProvidedServiceRequest containing information to create service
     * @throws Exception if service could not be created
     */
    void createProvidedService(UserAccount principal, ProvidedService createProvidedServiceRequest) throws Exception;

// RETRIEVE

    /**
     * @param employeeId to lookup services
     * @return all services provided by a specified employee
     */
    List<ProvidedService> retrieveEmployeesServices(Long employeeId);

    /**
     * @param serviceId to retrieve
     * @return a service's profile (including all stored content, cost, etc)
     * @throws Exception if service could not be retrieved
     */
    ProvidedServiceProfile retrieveServiceProfile(Long serviceId) throws Exception;

    /**
     * searches available services for matches
     *
     * @param searchText to match available services with
     * @return all services and their details that match search criteria
     */
    List<ProvidedServiceDetails> searchAvailableService(String searchText);

// UPDATE

    /**
     * uploads an image to be displayed on provided service profile
     *
     * @param principal of the employee who manages the service
     * @param serviceId to update
     * @param image to add to service profile
     * @throws Exception if image could not be uploaded
     */
    void uploadProvidedServiceImage(UserAccount principal, Long serviceId, MultipartFile image) throws Exception;

// DELETE

    /**
     * marks a provided service as unavailable
     *
     * @param principal the employee making the change
     * @param serviceId of the service to discontinue
     */
    void deleteProvidedService(UserAccount principal, Long serviceId);

}
