package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.AccountRequest;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserProfileEndpointTest extends ServiceTest {

    @Test
    void testRegisterUserEndpoint() throws Exception {

        final String username = "test";

        final var request = new AccountRequest(username, testingPassword, null, null);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(201))
        ;

        verify(userProfileDao).register(request.username(), request.password());
    }

    @Test
    void testRegisterUserEndpointDaoFails() throws Exception {

        final String username = "test";

        final var request = new AccountRequest(username, testingPassword, null, null);

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
    void testRegisterUserEndpointShortUsername() throws Exception {

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
    void testRegisterUserEndpointLongUsername() throws Exception {

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
    void testRegisterUserEndpointIllegalCharacterUsername() throws Exception {

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
    void testGetUserProfileEndpoint() throws Exception {

        final long userId = 1L;

        final String username = "test";

        when(userProfileDao.getUserProfile(username))
                .thenReturn(new UserProfileResponse(userId, username, ZonedDateTime.now().minusDays(3)));

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user")
                        .header("Authorization", testingToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(userId))))
                .andExpect(jsonPath("$.payload.username", Matchers.is(username)))
        ;
    }
}
