package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.eecs447.annotations.WithUserAccount;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.ContactInfo;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("BadRequestException")))
        ;
    }

    @Test
    @WithUserAccount
    void getUserProfile() throws Exception {

        when(userProfileDao.getContactInfo(testingUserId))
            .thenReturn(new ContactInfo("First", "Last",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", null, "City", "ST", "12345-6789", "Email"));

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user"))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user"))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().is(501))
        ;

        verify(userProfileDao).updateUserPassword(testingUserId, testingPassword, newPassword);
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
               .updateUserPassword(testingUserId, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao).updateUserPassword(testingUserId, testingPassword, newPassword);
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUserId, testingPassword, newPassword);
        verify(userProfileDao, never()).updateUserPassword(testingUserId, testingPassword, newPassword.toUpperCase());
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUserId, testingPassword, newPassword);
    }

    @Test    
    @WithUserAccount
    void updateUsername() throws Exception {

        final var newUsername = "new_username";

        final var request = String.format("""
        {
            "username":"%s"
        }""", newUsername);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(request))
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

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(request))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }
}
