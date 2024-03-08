package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.morriswa.salon.annotations.WithClientAccount;
import org.morriswa.salon.model.ClientInfo;
import org.springframework.http.HttpMethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class ClientEndpointTest extends ServiceTest {

    @Test
    @WithClientAccount
    void updateClientProfile() throws Exception {
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
    void updateUserProfileBadContactPreference() throws Exception {
        String request = """
            {
                "contactPreference": "Phonecall"
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
    void updateUserProfileShortPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
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
    void updateUserProfileLongPhoneNumber() throws Exception {
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
