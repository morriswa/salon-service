package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithUserAccount;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.ContactInfo;
import org.springframework.http.HttpMethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class UserProfileEndpointTest extends ServiceTest {

    @Test
    void registerUserEndpoint() throws Exception {

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", testingUsername, testingPassword);

        hit(HttpMethod.POST, "/register", request)
            .andExpect(status().is(201))
        ;

        verify(userProfileDao).register(testingUsername, testingPassword);
    }

    @Test
    void registerUserEndpointDaoFails() throws Exception {

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", testingUsername, testingPassword);

        doThrow(BadRequestException.class).when(userProfileDao)
                .register(testingUsername, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao).register(testingUsername, testingPassword);
    }

    @Test
    void registerUserEndpointShortUsername() throws Exception {

        final String username = "123";

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", username, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointLongUsername() throws Exception {

        final String username = "01234567012345670123456701234567012345670123456701234567012345678";

        assertEquals("Username is 65 characters long", username.length(), 65);

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", username, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointIllegalCharacterUsername() throws Exception {

        final String username = "will$";

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", username, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointShortPassword() throws Exception {

        final String shortPassword = "1234567";

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", testingUsername, shortPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfile() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(204));

        verify(userProfileDao).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileBadContactPreference() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Emai"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("contactPreference")));

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileMissingNameFields() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("firstName")))
            .andExpect(jsonPath("$.stack[1].field", Matchers.is("lastName")));

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileShortPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "123456789",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("phoneNumber")))
        ;

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileLongPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "12345678901",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("phoneNumber")))
        ;

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileBadPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "123456789$",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("phoneNumber")))
        ;

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }

    @Test
    @WithUserAccount
    void createUserProfileBadStateCode() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "K",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.stack[0].field", Matchers.is("stateCode")))
        ;

        verify(userProfileDao, never()).createUserContactInfo(any(), any());
    }
    
    @Test
    @WithUserAccount
    void createUserProfileDaoFailure() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        doThrow(BadRequestException.class).when(userProfileDao)
            .createUserContactInfo(testingUserId, info);

        hit(HttpMethod.POST, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("BadRequestException")))
        ;
    }

    @Test
    @WithUserAccount
    void updateUserProfile() throws Exception {
        String request = """
            {
                "firstName": "testing"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/user", request)
            .andExpect(status().is(204))
        ;

        verify(userProfileDao).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithUserAccount
    void updateUserProfileBadContactPreference() throws Exception {
        String request = """
            {
                "contactPreference": "Phonecall"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithUserAccount
    void updateUserProfileShortPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithUserAccount
    void updateUserProfileLongPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "11234567890"
            }
            """;

        ContactInfo info = mapper.readValue(request, ContactInfo.class);

        hit(HttpMethod.PATCH, "/user", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(userProfileDao, never()).updateUserContactInfo(testingUserId, info);
    }

    @Test
    @WithUserAccount
    void getUserProfile() throws Exception {

        when(userProfileDao.getContactInfo(testingUserId))
            .thenReturn(new ContactInfo("First", "Last",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", null, "City", "ST", "12345-6789", "Email"));

        hit(HttpMethod.GET, "/user")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(testingUserId))))
            .andExpect(jsonPath("$.payload.username", Matchers.is(testingUsername)))
            .andExpect(jsonPath("$.payload.address", Matchers.is("1234 Test Ave. City, ST 12345-6789")))
            .andExpect(jsonPath("$.payload.phoneNumber", Matchers.is("+1 (123) 456-7890")))
        ;
    }

    @Test
    @WithUserAccount
    void getUserProfileIncludingAddressLineTwo() throws Exception {

        when(userProfileDao.getContactInfo(testingUserId))
            .thenReturn(new ContactInfo("First", "Last",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", "Apt 567", "City", "ST", "12345-6789", "Email"));

        hit(HttpMethod.GET, "/user")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(testingUserId))))
            .andExpect(jsonPath("$.payload.username", Matchers.is(testingUsername)))
            .andExpect(jsonPath("$.payload.address", Matchers.is("1234 Test Ave. Apt 567 City, ST 12345-6789")))
            .andExpect(jsonPath("$.payload.phoneNumber", Matchers.is("+1 (123) 456-7890")))
        ;
    }

    @Test
    @WithUserAccount
    void updateUserPassword() throws Exception {

        final String newPassword = "password2";

        final var request = String.format("""
        {
            "currentPassword":"%s",
            "password":"%s",
            "confirmPassword":"%s"
        }""", testingPassword, newPassword, newPassword);

        hit(HttpMethod.PATCH, "/user/password", request)
            .andExpect(status().is(204))
        ;

        verify(userProfileDao).updateUserPassword(any(), any(), any(), any());
    }

    @Test
    @WithUserAccount
    void updateUserPasswordDaoFails() throws Exception {

        final String newPassword = "password2";

        final var request = String.format("""
        {
            "currentPassword":"%s",
            "password":"%s",
            "confirmPassword":"%s"
        }""", testingPassword, newPassword, newPassword);


        doThrow(BadRequestException.class).when(userProfileDao)
            .updateUserPassword(any(), any(), any(), any());

        hit(HttpMethod.PATCH, "/user/password", request)
            .andExpect(status().is(400))
        ;

        verify(userProfileDao).updateUserPassword(any(), any(), any(), any());
    }

    @Test
    @WithUserAccount
    void updateUserPasswordNotMatching() throws Exception {

        final String newPassword = "password2";

        final var request = String.format("""
        {
            "currentPassword":"%s",
            "password":"%s",
            "confirmPassword":"%s"
        }""", testingPassword, newPassword, newPassword.toUpperCase());

        hit(HttpMethod.PATCH, "/user/password")
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(any(), any(), any(), any());
    }

    @Test
    @WithUserAccount
    void updateUserPasswordShort() throws Exception {

        final String newPassword = "pass";

        final var request = String.format("""
        {
            "currentPassword":"%s",
            "password":"%s",
            "confirmPassword":"%s"
        }""", testingPassword, newPassword, newPassword);

        hit(HttpMethod.PATCH, "/user/password")
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(any(), any(), any(), any());
    }

    @Test    
    @WithUserAccount
    void updateUsername() throws Exception {

        final var newUsername = "new_username";

        final var request = String.format("""
        {
            "username":"%s"
        }""", newUsername);

        hit(HttpMethod.PATCH, "/user/name", request)
            .andExpect(status().is(204))
        ;

        verify(userProfileDao).changeUsername(testingUserId, newUsername);
    }

    @Test
    @WithUserAccount
    void updateUsernameDaoFails() throws Exception {

        final var duplicateUsername = "duplicate";

        final var request = String.format("""
        {
            "username":"%s"
        }""", duplicateUsername);

        doThrow(BadRequestException.class).when(userProfileDao)
               .changeUsername(testingUserId, duplicateUsername);

        hit(HttpMethod.PATCH, "/user/name", request)
            .andExpect(status().is(400))
        ;

        verify(userProfileDao).changeUsername(testingUserId, duplicateUsername);
    }

    @Test
    @WithUserAccount
    void updateUsernameShortUsername() throws Exception {

        final String username = "123";

        final var request = String.format("""
        {
            "username":"%s"
        }""", username);

        hit(HttpMethod.PATCH, "/user/name", request)
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void updateUsernameLongUsername() throws Exception {

        final String username = "01234567012345670123456701234567012345670123456701234567012345678";

        assertEquals("Username is 65 characters long", username.length(), 65);

        final var request = String.format("""
        {
            "username":"%s"
        }""", username);

        hit(HttpMethod.PATCH, "/user/name", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void updateUsernameIllegalCharacters() throws Exception {

        final String username = "will$";

        final var request = String.format("""
        {
            "username":"%s"
        }""", username);

        hit(HttpMethod.PATCH, "/user/name", request)
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }
}
