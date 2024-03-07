package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithClientAccount;
import org.morriswa.salon.model.ContactInfo;
import org.springframework.http.HttpMethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class ClientEndpointTest extends ServiceTest {

    @Test
    @WithClientAccount
    void updateUserProfile() throws Exception {
        String request = """
            {
                "firstName": "testing"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/client", request)
            .andExpect(status().is(204))
        ;

        verify(userProfileDao).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void updateUserProfileBadContactPreference() throws Exception {
        String request = """
            {
                "contactPreference": "Phonecall"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/client", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void updateUserProfileShortPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/client", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void updateUserProfileLongPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "11234567890"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/client", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void getUserProfile() throws Exception {

        when(userProfileDao.getContactInfo(testingUserId))
            .thenReturn(new ContactInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", null, "City", "ST", "12345-6789", "Email"));

        hit(HttpMethod.GET, "/client")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.userId", Matchers.is(Math.toIntExact(testingUserId))))
            .andExpect(jsonPath("$.username", Matchers.is(testingUsername)))
            .andExpect(jsonPath("$.address", Matchers.is("1234 Test Ave. City, ST 12345-6789")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithClientAccount
    void getUserProfileIncludingAddressLineTwo() throws Exception {

        when(userProfileDao.getContactInfo(testingUserId))
            .thenReturn(new ContactInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", "Apt 567", "City", "ST", "12345-6789", "Email"));

        hit(HttpMethod.GET, "/client")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.userId", Matchers.is(Math.toIntExact(testingUserId))))
            .andExpect(jsonPath("$.username", Matchers.is(testingUsername)))
            .andExpect(jsonPath("$.address", Matchers.is("1234 Test Ave. Apt 567 City, ST 12345-6789")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

}
