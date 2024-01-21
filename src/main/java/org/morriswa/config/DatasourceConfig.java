package org.morriswa.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; will provide all mysql config for the application
 */

@Configuration
@Profile("!test")
public class DatasourceConfig {

    private final Environment e;

    private final Logger log;


    @Autowired public DatasourceConfig(Environment e) {
        this.e = e;
        this.log = LoggerFactory.getLogger(DatasourceConfig.class);
    }


    /**
     * Attempts to build the Hikari Datasource to be used by the application for data-access
     * If datasource cannot be configured, service will halt
     *
     * @return a HikariDataSource bean that will be used by JDBC for Data Access
     */
    @Bean
    public HikariDataSource provideHikariDataSource() {

        final var dbUsername = e.getRequiredProperty("mysql.database.username");
        final var dbPassword = e.getRequiredProperty("mysql.database.password");
        final var jdbcUrl = String.format("%s://%s:%s/%s",
                e.getRequiredProperty("mysql.scheme"),
                e.getRequiredProperty("mysql.path"),
                e.getRequiredProperty("mysql.port"),
                e.getRequiredProperty("mysql.database.name"));

        var databaseConfig = new HikariConfig();
        databaseConfig.setUsername(dbUsername);
        databaseConfig.setPassword(dbPassword);
        databaseConfig.setJdbcUrl(jdbcUrl);

        try {
            return new HikariDataSource(databaseConfig);
        } catch (Exception e) {
            log.error("Could not configure database! Encountered the following errors, shutting down",e);
            throw new RuntimeException(e);
        }
    }
}
