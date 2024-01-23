package org.morriswa.eecs447;

import org.morriswa.eecs447.config.TestConfig;
import org.morriswa.eecs447.dao.ExampleDao;
import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "testing.password=test_password",
        "testing.token=Basic dGVzdDp0ZXN0X3Bhc3N3b3Jk"
})
public class ServiceTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected HttpResponseFactory responseFactory;

    @Autowired protected Environment e;

    @MockBean protected ExampleDao exampleDao;

    @MockBean protected UserProfileDao userProfileDao;

    @Value("${testing.token}") protected String testingToken;
}
