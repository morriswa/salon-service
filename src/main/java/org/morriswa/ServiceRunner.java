package org.morriswa;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

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
