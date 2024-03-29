package org.morriswa.salon.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@TestConfiguration
@EnableTransactionManagement
@EnableJdbcRepositories(basePackages = {
        "org.morriswa.salon.dao"
})
public class TestDatasourceConfig {

    private final Environment e;

    @Autowired
    public TestDatasourceConfig(Environment e) {
        this.e = e;
    }

    @Bean @Profile("test")
    public HikariDataSource getTestDatasource() {
        var dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl(e.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(e.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(e.getRequiredProperty("jdbc.password"));

        return dataSource;
    }
}
