package org.morriswa.salon.config;

import java.time.ZonedDateTime;

import org.morriswa.salon.model.AccountPermissions;
import org.morriswa.salon.model.UserAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; a mock authentication service for use during unit testing
 */

@TestConfiguration
@TestPropertySource("classpath:application-test.yml")
public class TestJdbcAuthenticationConfig {

    @Value("${testing.userId}") 
    private Long testingUserId;

    @Value("${testing.username}")
    private String testingUsername;

    @Value("${testing.password}")
    private String testingPassword;

    private final AccountPermissions userPermissions = new AccountPermissions(
            true,
            false,
            false,
            false
    );

    private final AccountPermissions clientPermissions = new AccountPermissions(
            true,
            true,
            false,
            false
    );

    private final AccountPermissions employeePermissions = new AccountPermissions(
            true,
            true,
            true,
            false
    );

    private final AccountPermissions adminPermissions = new AccountPermissions(
            true,
            true,
            true,
            true
    );

    @Bean("testUserAccount") @Primary
    public UserDetailsService getTestUserAccount() {
        return username -> new UserAccount(
            testingUserId,
            testingUsername,
            testingPassword,
            ZonedDateTime.now().minusDays(3),
            userPermissions);
    }

    @Bean("testClientAccount")
    public UserDetailsService getTestClientAccount() {
        return username -> new UserAccount(
            testingUserId,
            testingUsername,
            testingPassword,
            ZonedDateTime.now().minusDays(3),
            clientPermissions);
    }

    @Bean("testEmployeeAccount")
    public UserDetailsService getTestEmployeeAccount() {
        return username -> new UserAccount(
            testingUserId,
            testingUsername,
            testingPassword,
            ZonedDateTime.now().minusDays(3),
            employeePermissions);
    }

    @Bean("testAdminAccount")
    public UserDetailsService getTestAdminAccount() {
        return username -> new UserAccount(
            testingUserId,
            testingUsername,
            testingPassword,
            ZonedDateTime.now().minusDays(3),
            adminPermissions);
    }
}
