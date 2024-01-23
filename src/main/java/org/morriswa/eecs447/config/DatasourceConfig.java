package org.morriswa.eecs447.config;

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

@Configuration //Indicates to the scanner that this class describes the application config
@Profile("!test") //Indicates to the scanner that this class should be ignored during unit testing
public class DatasourceConfig //will provide all mysql config for the application
{ 
    @Autowired private Environment e; //Finds Enviroment and injects as dependency into class

    @Bean //Indicates to the scanner that anytime we need a HikariDataSource, use this method to generate it
    public HikariDataSource provideHikariDataSource() 
    {
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
