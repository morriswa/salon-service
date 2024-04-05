package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;

import java.util.List;

/**
 * provides an interface to manage provided service data in mysql
 *
 * @author William A. Morris
 * @since 2024-03-05
 */
public interface ProvidedServiceDao {
// CREATE

    /**
     * stores a provided service in mysql
     *
     * @param employeeId to register the service under
     * @param createProvidedServiceRequest containing information required to store service
     */
    void createProvidedService(Long employeeId, ProvidedService createProvidedServiceRequest);

// RETRIEVE

    /**
     * searches all provided services
     *
     * @param searchText to find related records
     * @return matching services with details
     */
    List<ProvidedServiceDetails> searchAvailableServices(String searchText);

    /**
     * retrieves all services provided by a specific employee
     *
     * @param employeeId to retrieve services for
     * @return all provided services
     */
    List<ProvidedService> retrieveEmployeesServices(Long employeeId);

    /**
     * @param serviceId to retrieve details for
     * @return all details about provided service
     * @throws BadRequestException if database query could not be executed
     *
     * @author Makenna Loewenherz
     */
    ProvidedServiceDetails retrieveServiceDetails(Long serviceId) throws BadRequestException;

    /**
     * retrieves all resource IDs associated with provided service images in s3 from mysql
     *
     * @param serviceId to retrieve content IDs for
     * @return resource codes to lookup content from bucket
     */
    List<String> retrieveServiceContent(Long serviceId);

    /**
     * determines if a service belongs to an employee
     *
     * @param serviceId to match
     * @param employeeId to match
     * @return whether the employee owns the service
     */
    boolean serviceBelongsTo(Long serviceId, Long employeeId);

    /**
     * @param contentId to check
     * @param serviceId owner
     * @return if a content ref belongs to a service
     */
    boolean contentBelongsToService(String contentId, Long serviceId);

// UPDATE

    /**
     * add resource code for service content to mysql
     *
     * @param serviceId to insert resource under
     * @param contentId corresponding to newly created resource
     */
    void addContentToProvidedService(Long serviceId, String contentId);

// DELETE

    /**
     * marks a service as not-bookable in mysql
     *
     * @param employeeId doing the action
     * @param serviceId to remove from available services
     */
    void deleteProvidedService(Long employeeId, Long serviceId);

    /**
     * removes all content from a service
     *
     * @param serviceId doing the action
     */
    void deleteProvidedServiceContent(Long serviceId);

    /**
     * removes specific content from a service
     *
     * @param serviceId from service
     * @param contentId to delete
     */
    void deleteProvidedServiceContent(Long serviceId, String contentId);

    /**
     * update provided service in mysql db
     *
     * @param employeeId authenticated employee
     * @param serviceId to update
     * @param request update params
     */
    void updateProvidedServiceDetails(Long employeeId, Long serviceId, ProvidedService request);
}
