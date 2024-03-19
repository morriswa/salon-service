package org.morriswa.salon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.TimeZone;

/**
 * will bootstrap and run the eecs447 project salon service
 *
 * @author William A. Morris
 * @since 2024-01-19
 */


// Enable configuration components annotated with @ConfigurationProperties
@EnableConfigurationProperties
// Do NOT use Spring Data Source Configuration,
// Data Source will be manually configured in org.morriswa.salon.config.DatasourceConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SalonService {
    public static void main(String[] args) {

        // Application time zone should be UTC, as that is what will run in prod.
        TimeZone.setDefault(TimeZone.getTimeZone("+00:00"));

        // create new spring application
        var application = new SpringApplication(SalonService.class);

        // add required application initializers (control things like environment)
        application.addInitializers(applicationContext -> {
            // add application initialization tasks here...
        });

        // run the application with provided CLI args
        application.run(args);
    }
}
