package org.morriswa.salon.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithClientAccount;
import org.morriswa.salon.model.ClientInfo;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class ClientServiceTest extends ServiceTest {

    @Test
    @WithClientAccount
    void updateFirstName() throws Exception {
        String request = """
            {
                "firstName": "testing"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyFirstName() throws Exception {
        String request = """
            {
                "firstName": " ",
                "lastName": "test"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("firstName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void updateLastName() throws Exception {
        String request = """
            {
                "lastName": "testing"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyLastName() throws Exception {
        String request = """
            {
                "firstName": "test",
                "lastName": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("lastName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void updateContactPreference() throws Exception {

        for (var pref : List.of("PhoneCall", "TextMessage", "Email")) {

            String request = String.format("""
                    {
                        "lastName": "test",
                        "contactPreference": "%s"
                    }
                    """, pref);

            hit(HttpMethod.PATCH, "/client/profile", request)
                    .andExpect(status().is(204))
            ;

        }

        verify(profileDao, times(3)).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectInvalidContactPreference() throws Exception {

        for (var pref : List.of("Phonecall", "textMessage", "email")) {

            String request = String.format("""
                    {
                        "lastName": "test",
                        "contactPreference": "%s"
                    }
                    """, pref);

            hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
                    .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
            ;
        }

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updatePhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1112223334"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
            .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectLongPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "11234567890"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectEmptyNumber() throws Exception {
        String request = """
            {
                "phoneNumber": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updateAddress() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updateAddressEmptyLineTwo() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": " ",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyAddressLineOne() throws Exception {
        String request = """
            {
                "addressLineOne": " ",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("addressLineOne")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyCity() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": " ",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("city")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "K"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectInvalidState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "K "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "KSS"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }


    @Test
    @WithClientAccount
    void updateZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "12345"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "1234"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "123456"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": " "
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }


    @Test
    @WithClientAccount
    void getClientProfile() throws Exception {

        when(profileDao.getClientInfo(testingUserId))
            .thenReturn(new ClientInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", null, "City", "ST", "12345",
                "Email", null));

        hit(HttpMethod.GET, "/client/profile")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithClientAccount
    void getUserProfileIncludingAddressLineTwo() throws Exception {

        when(profileDao.getClientInfo(testingUserId))
            .thenReturn(new ClientInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", "Apt 567", "City", "ST", "12345",
                "Email", null));

        hit(HttpMethod.GET, "/client/profile")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }


}
