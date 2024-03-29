package org.morriswa.salon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@TestConfiguration
public class TestDatasourceConfig {

    private final Environment e;

    @Autowired
    public TestDatasourceConfig(Environment e) {
        this.e = e;
    }

    @Bean
    public DataSource getTestDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(e.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(e.getRequiredProperty("jdbc.user"));
        dataSource.setPassword(e.getRequiredProperty("jdbc.pass"));

        return dataSource;
    }

    @Bean(name = "applicationJdbcTemplate") @Primary
    public NamedParameterJdbcTemplate applicationDataConnection(){
        return new NamedParameterJdbcTemplate(getTestDatasource());
    }

}
