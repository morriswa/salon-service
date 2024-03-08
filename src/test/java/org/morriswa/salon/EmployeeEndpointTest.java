package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithEmployeeAccount;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ProvidedServiceDetails;
import org.morriswa.salon.validation.ProvidedServiceValidator;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class EmployeeEndpointTest extends ServiceTest {

    @Test
    @WithEmployeeAccount
    void createProvidedService() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
            .andExpect(status().is(204));

        verify(providedServiceDao).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceBadCost() throws Exception {
        final var request = """
        {
            "cost": 3.1415,
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_INVALID_MONEY_VALUE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceBigCost() throws Exception {
        final var request = """
        {
            "cost": 1000.99,
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_BIG_MONEY_VALUE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceNoCost() throws Exception {
        final var request = """
        {
            "cost": 0.00,
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_OUT_OF_RANGE_MONEY_VALUE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }


    @Test
    @WithEmployeeAccount
    void createProvidedServiceNegativeCost() throws Exception {
        final var request = """
        {
            "cost": -0.01,
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_OUT_OF_RANGE_MONEY_VALUE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceMissingCost() throws Exception {
        final var request = """
        {
            "length": 2,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_MISSING_MONEY_VALUE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceMissingLength() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_MISSING_LENGTH)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceShortLength() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": 0,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_OUT_OF_RANGE_LENGTH)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceNegativeLength() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": -1,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_OUT_OF_RANGE_LENGTH)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceLongLength() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": 33,
            "name": "My Test Service"
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_OUT_OF_RANGE_LENGTH)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceMissingName() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": 32
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_NO_NAME_SERVICE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceBlankName() throws Exception {
        final var request = """
        {
            "cost": 19.99,
            "length": 32,
            "name": "     "
        }""";

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_NO_NAME_SERVICE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void createProvidedServiceLongName() throws Exception {

        String longName = "a".repeat(129);

        assert longName.length() == 129;

        final var request = String.format("""
        {
            "cost": 19.99,
            "length": 32,
            "name": "%s"
        }""", longName);

        hit(HttpMethod.POST, "/employee/service", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath(
                        "$.additionalInfo[0].message",
                        Matchers.is(ProvidedServiceValidator.ERROR_LONG_NAME_SERVICE)));

        verify(providedServiceDao, never()).createProvidedService(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void getServiceDetails() throws Exception {

        Long serviceId = 111L;

        final var testService = new ProvidedServiceDetails(serviceId, new BigDecimal("12.34"), 30, "My Service", null);

        when(providedServiceDao.retrieveServiceContent(serviceId))
                .thenReturn(List.of());

        when(providedServiceDao.retrieveServiceDetails(serviceId))
                .thenReturn(testService);

        hit(HttpMethod.GET, String.format("/shared/service/%d",serviceId))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.serviceId",
                        Matchers.hasToString(serviceId.toString())))
                .andExpect(jsonPath("$.name", Matchers.is(testService.getName())))
                .andExpect(jsonPath("$.cost",
                        Matchers.hasToString(testService.getCost().toString())))
                .andExpect(jsonPath("$.length", Matchers.is(testService.getLength())))
        ;

        verify(providedServiceDao).retrieveServiceDetails(any());

        verify(providedServiceDao).retrieveServiceContent(any());
    }

    @Test
    @WithEmployeeAccount
    void getServiceDetailsBadService() throws Exception {

        Long serviceId = 111L;

        final var testService = new ProvidedServiceDetails(serviceId, new BigDecimal("12.34"), 30, "My Service",null);

        when(providedServiceDao.retrieveServiceDetails(serviceId))
                .thenThrow(BadRequestException.class);

        hit(HttpMethod.GET, String.format("/shared/service/%d",serviceId))
                .andExpect(status().is(400))
        ;

        verify(providedServiceDao).retrieveServiceDetails(any());
        verify(providedServiceDao, never()).retrieveServiceContent(any());
    }
}
