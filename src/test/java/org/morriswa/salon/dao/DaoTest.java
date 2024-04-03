package org.morriswa.salon.dao;

import org.morriswa.salon.config.TestConfig;
import org.morriswa.salon.config.TestDatasourceConfig;
import org.morriswa.salon.config.TestJdbcAuthenticationConfig;
import org.morriswa.salon.utility.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {TestConfig.class, TestDatasourceConfig.class, TestJdbcAuthenticationConfig.class})
@ActiveProfiles({"test", "h2test"})
@TestPropertySource({"classpath:application-test.yml"})
@Transactional
public class DaoTest {

    @Autowired protected AccountDao accountDao;

    @Autowired protected ProfileDao profileDao;

    @Autowired protected ProvidedServiceDao providedServiceDao;

    @Autowired protected ScheduleDao scheduleDao;

    @Autowired protected NamedParameterJdbcTemplate jdbcTemplate;

    @MockBean protected AmazonS3Client amazonS3Client;
}
