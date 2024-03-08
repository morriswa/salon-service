package org.morriswa.salon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mysql")
@Getter @Setter
public class MySQLProperties {
    private String protocol;
    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;
    private List<String> connectionProperties;
}
