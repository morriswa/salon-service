package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void getUserProfileEndpoint() throws Exception {

        final long userId = 1L;

        when(userProfileDao.getUserProfile(testingUsername))
                .thenReturn(new UserProfileResponse(userId, testingUsername, ZonedDateTime.now().minusDays(3)));

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user")
                        .header("Authorization", testingToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(userId))))
                .andExpect(jsonPath("$.payload.username", Matchers.is(testingUsername)))
        ;
    }

    @Test
    void updateUserPassword() throws Exception {

        final String newPassword = "password2";

        var request = new AccountRequest(null, newPassword, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
                        .header("Authorization", testingToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(501))
        ;

        verify(userProfileDao).updateUserPassword(testingUsername, testingPassword, newPassword);
    }

    @Test
    void updateUserPasswordNotMatching() throws Exception {

        final String newPassword = "password2";

        var request = new AccountRequest(null, newPassword, testingPassword, "Password2");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
                        .header("Authorization", testingToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUsername, testingPassword, newPassword);
    }

    @Test
    void updateUserPasswordShort() throws Exception {

        final String newPassword = "pass";

        var request = new AccountRequest(null, newPassword, testingPassword, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PATCH, "/user/password")
                        .header("Authorization", testingToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(400))
        ;

        verify(userProfileDao, never()).updateUserPassword(testingUsername, testingPassword, newPassword);
    }
}
