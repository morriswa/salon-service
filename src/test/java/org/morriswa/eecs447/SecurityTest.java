package org.morriswa.eecs447;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTest extends ServiceTest {

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/health"))
                .andExpect(status().is(401))
        ;
    }

    @Test
    void testAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/health")
                .header("Authorization", testingToken))
                .andExpect(status().is(200))
        ;
    }
}