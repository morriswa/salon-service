package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserProfileTest extends ServiceTest {

    @Value("${testing.token}")
    private String testingToken;

    @Test
    void testGetUserIdEndpoint() throws Exception {

        final long userId = 1L;

        final String username = "test";

        when(userProfileDao.getUserId(username))
                .thenReturn(userId);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user")
                        .header("Authorization", testingToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.payload", Matchers.is(Math.toIntExact(userId))))
        ;
    }
}
