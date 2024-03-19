package org.morriswa.salon.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * will provide a Hikari Datasource to be used by JDBC
 *
 * @author William A. Morris
 * @since 2024-01-19
 */
@Configuration //Indicates to the scanner that this class describes the application config
@Profile("!test") //Indicates to the scanner that this class should be ignored during unit testing
public class DatasourceConfig {  //will provide all mysql config for the application

    private final MySQLProperties mysql;

    @Autowired public DatasourceConfig(MySQLProperties config) {
        this.mysql = config;
    }


    /**
     * Attempts to build the Hikari Datasource to be used by the application for data-access
     * If datasource cannot be configured, service will halt
     *
     * @return a HikariDataSource bean that will be used by JDBC for Data Access
     */
    @Bean
    public HikariDataSource provideHikariDataSource() {

        // retrieve database username and password from application config
        final var dbUsername = mysql.getUsername();
        final var dbPassword = mysql.getPassword();

        // build jdbc connection string
        final String jdbcUrl;
        {
            // using provided protocol, hostname, port and database
            var connectionString = new StringBuilder(String.format("%s://%s:%s/%s",
                    mysql.getProtocol(),
                    mysql.getHostname(),
                    mysql.getPort(),
                    mysql.getDatabase()));

            // if additional connection params are required by application
            if (!mysql.getConnectionProperties().isEmpty()) {

                // add all params
                connectionString.append("?");
                for (String prop : mysql.getConnectionProperties())
                    connectionString.append(String.format("%s&", prop));

                // and remove tailing &
                jdbcUrl = connectionString.substring(0, connectionString.length() - 1);
            } else { // if no additional connection params are required, return as is
                jdbcUrl = connectionString.toString();
            }
        }

        // input retrieved properties into Hikari Config model
        var databaseConfig = new HikariConfig();
        databaseConfig.setUsername(dbUsername);
        databaseConfig.setPassword(dbPassword);
        databaseConfig.setJdbcUrl(jdbcUrl);

        // build and return configured datasource
        return new HikariDataSource(databaseConfig);
    }
}
