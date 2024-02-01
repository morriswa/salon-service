package org.morriswa.eecs447;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.morriswa.eecs447.config.TestConfig;
import org.morriswa.eecs447.config.TestJdbcAuthenticationConfig;
import org.morriswa.eecs447.dao.EmployeeDao;
import org.morriswa.eecs447.dao.ExampleDao;
import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.utility.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {TestConfig.class, TestJdbcAuthenticationConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yml")
@ExtendWith(SpringExtension.class) 
@ContextConfiguration
public class ServiceTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper mapper;

    @Autowired protected Environment e;

    @MockBean protected ExampleDao exampleDao;

    @MockBean protected UserProfileDao userProfileDao;

    @MockBean protected EmployeeDao employeeDao;

    @MockBean protected AmazonS3Client amazonS3Client;

    @Value("${testing.bad-token}") protected String badToken;

    @Value("${testing.userId}") protected Long testingUserId;

    @Value("${testing.username}") protected String testingUsername;

    @Value("${testing.password}") protected String testingPassword;
}
