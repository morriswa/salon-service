package org.morriswa;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; will bootstrap and run the eecs447 project service
 */

// Do NOT use Spring Data Source Configuration,
// Data Source will be manually configured in org.morriswa.config.DatasourceConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceRunner {
    public static void main(String[] args) {
        // create new spring application
        var application = new SpringApplicationBuilder()
                .sources(ServiceRunner.class)
                .initializers(applicationContext -> {
                    // add application initialization tasks here
                });
        // run application
        application.run(args);
    }
}
