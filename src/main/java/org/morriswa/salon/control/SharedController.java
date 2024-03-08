package org.morriswa.salon.control;

import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.model.PublicEmployeeProfileResponse;
import org.morriswa.salon.service.ProfileService;
import org.morriswa.salon.service.ProvidedServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-25 <br>
 * PURPOSE: <br>
 * &emsp; provides a REST API for performing employee/business functions that can be consumed by other applications
 */

@RestController
public class SharedController {

    private final ProvidedServiceService providedServices;
    private final ProfileService profileService;

    @Autowired
    public SharedController(ProvidedServiceService providedServices, ProfileService profileService) {
        this.providedServices = providedServices;
        this.profileService = profileService;
    }


    /**
     * Http GET endpoint used to retrieve all stored information about the currently authenticated user
     *
     * @return profile and contact information about the user if operation was successful, else error response
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<PublicEmployeeProfileResponse> getPublicEmployeeProfile(
            @PathVariable Long employeeId
    ) throws Exception{
        // using the user profile service, retrieve the current users profile
        var profile = profileService.getPublicEmployeeProfile(employeeId);
        // and return it to them in JSON format
        return ResponseEntity.ok(profile);
    }


    /**
     * HTTP Get endpoint for employees to view all the services they are providing to users
     *
     * @return an array of provided services
     * @throws Exception return error response if employee's services could not be retrieved
     */
    @GetMapping("/employee/{employeeId}/services")
    public ResponseEntity<List<ProvidedService>> retrieveAllProvidedServices(
            @PathVariable Long employeeId
    ) throws Exception {
        var services = providedServices.retrieveEmployeesServices(employeeId);
        return ResponseEntity.ok(services);
    }

    /**
     * Http GET endpoint for employees to retrieve all stored information about a provided service
     *
     * @param serviceId associated with the service
     * @return all information about requested service
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ProvidedServiceDetails> getProvidedServiceDetails(
        @PathVariable Long serviceId
    ) throws Exception {
        var profile = providedServices.retrieveServiceProfile(serviceId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/services")
    public ResponseEntity<List<ProvidedServiceDetails>> searchAvailableService(
            @RequestParam String searchText
    ) throws Exception {
        final var services = providedServices.searchAvailableService(searchText);
        return ResponseEntity.ok(services);
    }
}
