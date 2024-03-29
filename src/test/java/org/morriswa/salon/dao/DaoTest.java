package org.morriswa.salon.dao;

import org.junit.jupiter.api.extension.ExtendWith;
import org.morriswa.salon.config.TestConfig;
import org.morriswa.salon.config.TestDatasourceConfig;
import org.morriswa.salon.config.TestJdbcAuthenticationConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = {TestConfig.class, TestDatasourceConfig.class, TestJdbcAuthenticationConfig.class})
@ActiveProfiles("test")
@TestPropertySource({"classpath:application-test.yml"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@AutoConfigureMockMvc
public class DaoTest {

}
