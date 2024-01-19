package org.morriswa.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
    @Autowired private Environment e;

    @Bean
    public HikariDataSource provideHikariDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class)
            .username(e.getRequiredProperty("mysql.database.username"))
            .password(e.getRequiredProperty("mysql.database.password"))
            .url(String.format("%s://%s:%s/%s",
                    e.getRequiredProperty("mysql.scheme"),
                    e.getRequiredProperty("mysql.path"),
                    e.getRequiredProperty("mysql.port"),
                    e.getRequiredProperty("mysql.database.name"))
            ).build();
    }
}
