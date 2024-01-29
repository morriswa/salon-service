package org.morriswa.eecs447.config;

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

@Configuration //Indicates to the scanner that this class describes the application config
@Profile("!test") //Indicates to the scanner that this class should be ignored during unit testing
public class DatasourceConfig {  //will provide all mysql config for the application

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

        final var dbUsername = e.getRequiredProperty("mysql.username");
        final var dbPassword = e.getRequiredProperty("mysql.password");
        final var jdbcUrl = String.format("%s://%s:%s/%s",
                e.getRequiredProperty("mysql.protocol"),
                e.getRequiredProperty("mysql.hostname"),
                e.getRequiredProperty("mysql.port"),
                e.getRequiredProperty("mysql.database"));

        var databaseConfig = new HikariConfig();
        databaseConfig.setUsername(dbUsername);
        databaseConfig.setPassword(dbPassword);
        databaseConfig.setJdbcUrl(jdbcUrl);

        return new HikariDataSource(databaseConfig);
    }
}
