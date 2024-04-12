package org.morriswa.salon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.morriswa.salon.config.TestConfig;
import org.morriswa.salon.dao.AccountDao;
import org.morriswa.salon.dao.ProfileDao;
import org.morriswa.salon.dao.ProvidedServiceDao;
import org.morriswa.salon.dao.ScheduleDao;
import org.morriswa.salon.utility.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


/**
 * test configuration for controller and service unit tests
 *
 * @author William A. Morris
 * @since 2024-01-22
 */
@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ServiceTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper mapper;

    @Autowired protected Environment e;

    @MockBean protected AccountDao accountDao;

    @MockBean protected ProfileDao profileDao;

    @MockBean protected ProvidedServiceDao providedServiceDao;

    @MockBean protected ScheduleDao scheduleDao;

    @MockBean protected AmazonS3Client amazonS3Client;

    @Value("${testing.bad-token}") protected String badToken;

    @Value("${testing.userId}") protected Long testingUserId;

    @Value("${testing.username}") protected String testingUsername;

    @Value("${testing.password}") protected String testingPassword;

    protected ResultActions hit(HttpMethod method, String endpoint) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.request(method, endpoint));
    }

    protected ResultActions hit(HttpMethod method, String endpoint, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.request(method, endpoint)
            .contentType("application/json")
            .content(body));
    }

}
