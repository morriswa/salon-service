package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithNewUserAccount;
import org.morriswa.salon.annotations.WithUserAccount;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.UserInfo;
import org.springframework.http.HttpMethod;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountEndpointTest extends ServiceTest{

    @Test
    void registerUserEndpoint() throws Exception {

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", testingUsername, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(204))
        ;

        verify(accountDao).register(testingUsername, testingPassword);
    }

    @Test
    void registerUserEndpointDaoFails() throws Exception {

        final var request = String.format("""
        {
            "username":"%s",
            "password":"%s"
        }""", testingUsername, testingPassword);

        doThrow(BadRequestException.class).when(accountDao)
                .register(testingUsername, testingPassword);

        hit(HttpMethod.POST, "/register", request)
                .andExpect(status().is(400))
        ;

        verify(accountDao).register(testingUsername, testingPassword);
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

        verify(accountDao, never()).register(any(), any());
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

        verify(accountDao, never()).register(any(), any());
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

        verify(accountDao, never()).register(any(), any());
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

        verify(accountDao, never()).register(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfile() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(204));

        verify(accountDao).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileBadContactPreference() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044",
                "contactPreference": "Emai"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("contactPreference")));

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
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

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("pronouns")))
                .andExpect(jsonPath("$.additionalInfo[1].field", Matchers.is("firstName")))
                .andExpect(jsonPath("$.additionalInfo[2].field", Matchers.is("lastName")));

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileShortPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "123456789",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileLongPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "12345678901",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileBadPhoneNumber() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "123456789$",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileBadStateCode() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "K",
                "zipCode": "66044-1234",
                "contactPreference": "Email"
            }
            """;

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(accountDao, never()).enterContactInfo(any(), any());
    }

    @Test
    @WithNewUserAccount
    void createUserProfileDaoFailure() throws Exception {
        String request = """
            {
                "firstName": "testing",
                "lastName": "test",
                "pronouns": "T",
                "phoneNumber": "1234567890",
                "email": "test@email.com",
                "addressLineOne": "12345 Easy St.",
                "city": "Lawrence",
                "stateCode": "KS",
                "zipCode": "66044",
                "contactPreference": "Email"
            }
            """;

        doThrow(BadRequestException.class).when(accountDao)
                .enterContactInfo(eq(testingUserId), any(UserInfo.class));

        hit(HttpMethod.POST, "/r2/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("BadRequestException")))
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

        verify(accountDao).updateUserPassword(any(), any(), any(), any());
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


        doThrow(BadRequestException.class).when(accountDao)
                .updateUserPassword(any(), any(), any(), any());

        hit(HttpMethod.PATCH, "/user/password", request)
                .andExpect(status().is(400))
        ;

        verify(accountDao).updateUserPassword(any(), any(), any(), any());
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

        verify(accountDao, never()).updateUserPassword(any(), any(), any(), any());
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

        verify(accountDao, never()).updateUserPassword(any(), any(), any(), any());
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

        verify(accountDao).changeUsername(testingUserId, newUsername);
    }

    @Test
    @WithUserAccount
    void updateUsernameDaoFails() throws Exception {

        final var duplicateUsername = "duplicate";

        final var request = String.format("""
        {
            "username":"%s"
        }""", duplicateUsername);

        doThrow(BadRequestException.class).when(accountDao)
                .changeUsername(testingUserId, duplicateUsername);

        hit(HttpMethod.PATCH, "/user/name", request)
                .andExpect(status().is(400))
        ;

        verify(accountDao).changeUsername(testingUserId, duplicateUsername);
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

        verify(accountDao, never()).register(any(), any());
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

        verify(accountDao, never()).register(any(), any());
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

        verify(accountDao, never()).register(any(), any());
    }

}
