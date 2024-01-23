package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserProfileTest extends ServiceTest {

    @Test
    void testGetUserProfile() throws Exception {

        final long userId = 1L;

        final String username = "test";

        when(userProfileDao.getUserProfile(username))
                .thenReturn(new UserProfileResponse(userId, username));

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user")
                        .header("Authorization", testingToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload.userId", Matchers.is(Math.toIntExact(userId))))
                .andExpect(jsonPath("$.payload.username", Matchers.is(username)))
        ;
    }
}
