package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.model.ProvidedService;

import java.math.BigDecimal;

import static org.springframework.test.util.AssertionErrors.*;

public class ProvidedServiceDaoTest extends DaoTest {

    @Test
    public void createProvidedServiceRetrieveEmployeesServicesQuery() {

        final Long employeeId = 21L;

        final ProvidedService newService
                = new ProvidedService(null, new BigDecimal("123.45"), 2, "Test Create Service 1");

        providedServiceDao.createProvidedService(employeeId, newService);

        var services = providedServiceDao.retrieveEmployeesServices(employeeId);

        assertNotNull("services should not be null", services);

        final var generatedService = services
                .stream()
                .filter(providedService -> providedService.getName().equals(newService.getName()))
                .findFirst();

        assertTrue("service should've been found", generatedService.isPresent());

        final Long serviceId = generatedService.get().getServiceId();

        assertNotNull("service id should've been generated", serviceId);
        assertTrue("service should belong to employee", providedServiceDao.serviceBelongsTo(serviceId, employeeId));
        assertEquals("service name should be stored correctly",
                newService.getName(), generatedService.get().getName());
        assertEquals("service cost should be stored correctly",
                newService.getCost(), generatedService.get().getCost());
        assertEquals("service length should be stored and retrieved correctly",
                30, generatedService.get().getLength());
    }

    @Test
    public void serviceBelongsToQuery() {

        final Long employeeId1 = 21L;
        final Long employeeId2 = 22L;

        assertFalse("fake service should not belong to employee",
                providedServiceDao.serviceBelongsTo(1L, employeeId1));

        final ProvidedService newService
                = new ProvidedService(null, new BigDecimal("123.45"), 2, "Test Create Service 1");

        providedServiceDao.createProvidedService(employeeId2, newService);

        var services = providedServiceDao.retrieveEmployeesServices(employeeId2);

        final var generatedService = services
                .stream()
                .filter(providedService -> providedService.getName().equals(newService.getName()))
                .findFirst();

        assertTrue("created service should've been found", generatedService.isPresent());

        final Long serviceId = generatedService.get().getServiceId();

        assertTrue("new service should belong to correct employee", providedServiceDao.serviceBelongsTo(serviceId, employeeId2));
        assertFalse("new service should belong to correct employee", providedServiceDao.serviceBelongsTo(serviceId, employeeId1));
    }
}
