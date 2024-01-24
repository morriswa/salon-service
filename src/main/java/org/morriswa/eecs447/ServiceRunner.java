package org.morriswa.eecs447;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; will bootstrap and run the eecs447 project service
 */

// Do NOT use Spring Data Source Configuration,
// Data Source will be manually configured in org.morriswa.eecs447.config.DatasourceConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceRunner {
    public static void main(String[] args) {

        // create new spring application
        var application = new SpringApplication(ServiceRunner.class);

        // add required application initializers (control things like environment)
        application.addInitializers(applicationContext -> {
            // add application initialization tasks here...

            // if the user has specified a runtime environment...
            if (System.getenv("RUNTIME_ENV")!=null) {
                
                // retrieve env code
                final String runtimeEnvironment = System.getenv("RUNTIME_ENV");
            
                // and run appropriate application context configuration 
                switch (runtimeEnvironment) {
                    case "LOCAL_DOCKER":
                        // if running the application locally as a docker container...
                        applicationContext.getEnvironment().getPropertySources()
                            // add property source to environment that will override default settings
                            .addFirst(new PropertiesPropertySource("LOCAL_DOCKER_OVERRIDES", new Properties(){{
                                // local docker containers use a different path to connect to the database
                                // override path config
                                put("mysql.path", "host.docker.internal");
                            }}));
                        break;
                    default:
                        break;
                }
            }
                
        });

        // run the application with provided CLI args
        application.run(args);
    }
}
