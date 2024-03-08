package org.morriswa.salon.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; will provide all mysql config for the application
 */

@Configuration //Indicates to the scanner that this class describes the application config
@Profile("!test") //Indicates to the scanner that this class should be ignored during unit testing
public class DatasourceConfig {  //will provide all mysql config for the application

    private final MySQLProperties config;

    @Autowired public DatasourceConfig(MySQLProperties config) {
        this.config = config;
    }


    /**
     * Attempts to build the Hikari Datasource to be used by the application for data-access
     * If datasource cannot be configured, service will halt
     *
     * @return a HikariDataSource bean that will be used by JDBC for Data Access
     */
    @Bean
    public HikariDataSource provideHikariDataSource() {

        final var dbUsername = config.getUsername();
        final var dbPassword = config.getPassword();
        final String jdbcUrl;
        {
            var urlBuilder = new StringBuilder(String.format("%s://%s:%s/%s",
                    config.getProtocol(),
                    config.getHostname(),
                    config.getPort(),
                    config.getDatabase()));

            if (!config.getConnectionProperties().isEmpty()) {
                urlBuilder.append("?");

                for (String prop : config.getConnectionProperties())
                    urlBuilder.append(String.format("%s&", prop));

                jdbcUrl = urlBuilder.substring(0, urlBuilder.length() - 1);
            } else {
                jdbcUrl = urlBuilder.toString();
            }
        }

        var databaseConfig = new HikariConfig();
        databaseConfig.setUsername(dbUsername);
        databaseConfig.setPassword(dbPassword);
        databaseConfig.setJdbcUrl(jdbcUrl);

        return new HikariDataSource(databaseConfig);
    }
}
