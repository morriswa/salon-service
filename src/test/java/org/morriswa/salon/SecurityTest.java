package org.morriswa.salon;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class SecurityTest extends ServiceTest {

    @Test
    void unauthenticatedRequest() throws Exception {
        hit(HttpMethod.GET, "/health")
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.error",
                Matchers.is(InsufficientAuthenticationException.class.getSimpleName())))
        ;
    }

    @Test
    void badCredentialsRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/health")
            .header("Authorization", badToken))
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.error",
                Matchers.is(BadCredentialsException.class.getSimpleName())))
        ;
    }

    @Test
    @WithUserDetails
    void authenticatedRequest() throws Exception {
        hit(HttpMethod.GET, "/health")
            .andExpect(status().is(200))
        ;
    }

    @Test
    @WithUserDetails
    void accessDeniedRequest() throws Exception {
        hit(HttpMethod.GET, "/employee/schedule")
            .andExpect(status().is(403))
        ;
    }
}