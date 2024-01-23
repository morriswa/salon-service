package org.morriswa.eecs447.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@TestConfiguration
public class TestConfig {

    @Bean @Profile("test")
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return (String) rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword);
            }
        };
    }

    @Bean @Profile("test")
    public BuildProperties getTestBuildProps() {
        // This anonymous class needs for the possibility of using SpyBean in test methods
        // Lambda cannot be a spy with spring @SpyBean annotation
        return new BuildProperties(new Properties(){{
            put("name", "eecs447-project-test");
            put("version", "testing-testing-123");
        }});
    }
}