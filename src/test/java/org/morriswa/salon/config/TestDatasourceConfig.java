package org.morriswa.salon.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
public class TestDatasourceConfig {

    private final MySQLProperties mysql;

    @Autowired
    public TestDatasourceConfig(MySQLProperties mysql) {
        this.mysql = mysql;
    }

    @Bean @Profile("test")
    public HikariDataSource getTestDatasource() {
        var dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");

        StringBuilder connectionString = new StringBuilder(
            String.format("%s:%s",
                mysql.getProtocol(),
                mysql.getDatabase())
        );
        final String jdbcUrl;

        // if additional connection params are required by application
        if (!mysql.getConnectionProperties().isEmpty()) {
            // add all params
            connectionString.append(";");
            for (String prop : mysql.getConnectionProperties())
                connectionString.append(String.format("%s;", prop));
        }

        jdbcUrl = connectionString.toString();

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(mysql.getUsername());
        dataSource.setPassword(mysql.getPassword());

        return dataSource;
    }
}
