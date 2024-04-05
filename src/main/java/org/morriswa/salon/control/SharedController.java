package org.morriswa.salon.control;

import org.morriswa.salon.model.ProvidedService;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.model.PublicEmployeeProfile;
import org.morriswa.salon.service.ProfileService;
import org.morriswa.salon.service.ProvidedServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * provides a REST API for performing client and employee tasks
 *
 * @author William A. Morris
 * @since 2024-01-25
 */

@RestController @RequestMapping("/shared")
public class SharedController {

    private final ProvidedServiceService providedServices;

    @Autowired
    public SharedController(ProvidedServiceService providedServices) {
        this.providedServices = providedServices;
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

    /**
     * HTTP Get endpoint to search available services
     *
     * @param searchText to return results for
     * @return list of provided services
     */
    @GetMapping("/services")
    public ResponseEntity<List<ProvidedServiceDetails>> searchAvailableService(
            @RequestParam String searchText
    ) {
        final var services = providedServices.searchAvailableService(searchText);
        return ResponseEntity.ok(services);
    }
}
