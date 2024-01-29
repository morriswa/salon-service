package org.morriswa.eecs447;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTest extends ServiceTest {

    @Test
    void unauthorizedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/health"))
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.error",
                        Matchers.is(InsufficientAuthenticationException.class.getSimpleName())))
        ;
    }

    @Test
    void authorizedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/health")
                .header("Authorization", testingToken))
                .andExpect(status().is(200))
        ;
    }

    @Test
    void forbiddenResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/fake-not-real")
                        .header("Authorization", testingToken))
                .andExpect(status().is(403))
        ;
    }
}