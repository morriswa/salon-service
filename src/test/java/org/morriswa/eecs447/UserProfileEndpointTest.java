package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.eecs447.annotations.WithUserAccount;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.AccountRequest;
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

        final var request = new AccountRequest(testingUsername, testingPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(201))
        ;

        verify(userProfileDao).register(request.username(), request.password());
    }

    @Test
    void registerUserEndpointDaoFails() throws Exception {

        final var request = new AccountRequest(testingUsername, testingPassword, null, null);

        doThrow(BadRequestException.class).when(userProfileDao)
                .register(request.username(),request.password());

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao).register(request.username(), request.password());
    }

    @Test
    void registerUserEndpointShortUsername() throws Exception {

        final String username = "123";

        final var request = new AccountRequest(username, testingPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointLongUsername() throws Exception {

        final String username = "01234567012345670123456701234567012345670123456701234567012345678";

        assertEquals("Username is 65 characters long", username.length(), 65);

        final var request = new AccountRequest(username, testingPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointIllegalCharacterUsername() throws Exception {

        final String username = "will$";

        final var request = new AccountRequest(username, testingPassword, null, null );

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    void registerUserEndpointShortPassword() throws Exception {

        final String shortPassword = "1234567";

        final var request = new AccountRequest(testingUsername, shortPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void getUserProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user"))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(testingUserId))))
            .andExpect(jsonPath("$.payload.username", Matchers.is(testingUsername)))
        ;
    }

    @Test
    @WithUserAccount
    void updateUserPassword() throws Exception {

        final String newPassword = "password2";

        var request = new AccountRequest(null, newPassword, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(501))
        ;

        verify(userProfileDao).updateUserPassword(testingUserId, testingPassword, newPassword);
    }

    @Test
    @WithUserAccount
    void updateUserPasswordDaoFails() throws Exception {

        final String newPassword = "password2";

        var request = new AccountRequest(null, newPassword, testingPassword, newPassword);

        doThrow(BadRequestException.class).when(userProfileDao)
               .updateUserPassword(testingUserId, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao).updateUserPassword(testingUserId, testingPassword, newPassword);
    }

    @Test
    @WithUserAccount
    void updateUserPasswordNotMatching() throws Exception {

        final String newPassword = "password2";

        var request = new AccountRequest(null, newPassword, testingPassword, "Password2");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUserId, testingPassword, newPassword);
    }

    @Test
    @WithUserAccount
    void updateUserPasswordShort() throws Exception {

        final String newPassword = "pass";

        var request = new AccountRequest(null, newPassword, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUserId, testingPassword, newPassword);
    }

    @Test    
    @WithUserAccount
    void updateUsername() throws Exception {

        final var newUsername = "new_username";

        final var request = new AccountRequest(newUsername, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(501))
        ;

        verify(userProfileDao).changeUsername(testingUserId, newUsername);
    }

    @Test
    @WithUserAccount
    void updateUsernameDaoFails() throws Exception {

        final var duplicateUsername = "duplicate";

        final var request = new AccountRequest(duplicateUsername, null, null, null);

        doThrow(BadRequestException.class).when(userProfileDao)
               .changeUsername(testingUserId, duplicateUsername);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao).changeUsername(testingUserId, duplicateUsername);
    }

    @Test
    @WithUserAccount
    void updateUsernameShortUsername() throws Exception {

        final String username = "123";

        final var request = new AccountRequest(username, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void updateUsernameLongUsername() throws Exception {

        final String username = "01234567012345670123456701234567012345670123456701234567012345678";

        assertEquals("Username is 65 characters long", username.length(), 65);

        final var request = new AccountRequest(username, testingPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }

    @Test
    @WithUserAccount
    void updateUsernameIllegalCharacters() throws Exception {

        final String username = "will$";

        final var request = new AccountRequest(username, testingPassword, null, null );

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/name")
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).register(any(), any());
    }
}
