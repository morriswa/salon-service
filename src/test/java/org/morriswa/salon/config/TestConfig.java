package org.morriswa.salon.config;

import org.morriswa.salon.model.UserAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.Set;

/**
 * provides required beans for test runs ONLY
 */
@TestConfiguration
@TestPropertySource("classpath:application-test.yml")
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
        return new BuildProperties(new Properties(){{
            put("name", "eecs447-project-test");
            put("version", "testing-testing-123");
        }});
    }

    @Value("${testing.userId}")
    private Long testingUserId;

    @Value("${testing.username}")
    private String testingUsername;

    @Value("${testing.password}")
    private String testingPassword;


    @Bean("testUserAccount") @Primary
    public UserDetailsService getTestUserAccount() {
        return username -> new UserAccount(
                testingUserId,
                testingUsername,
                testingPassword,
                ZonedDateTime.now().minusDays(3),
                Set.of(new SimpleGrantedAuthority("USER")
                ));
    }

    @Bean("testNewUserAccount")
    public UserDetailsService getTestNewUserAccount() {
        return username -> new UserAccount(
                testingUserId,
                testingUsername,
                testingPassword,
                ZonedDateTime.now().minusDays(3),
                Set.of(new SimpleGrantedAuthority("NUSER")
                ));
    }

    @Bean("testClientAccount")
    public UserDetailsService getTestClientAccount() {
        return username -> new UserAccount(
                testingUserId,
                testingUsername,
                testingPassword,
                ZonedDateTime.now().minusDays(3),
                Set.of(
                        new SimpleGrantedAuthority("USER"),
                        new SimpleGrantedAuthority("CLIENT")
                )
        );
    }

    @Bean("testEmployeeAccount")
    public UserDetailsService getTestEmployeeAccount() {
        return username -> new UserAccount(
                testingUserId,
                testingUsername,
                testingPassword,
                ZonedDateTime.now().minusDays(3),
                Set.of(
                        new SimpleGrantedAuthority("USER"),
                        new SimpleGrantedAuthority("EMPLOYEE")
                )
        );
    }
}