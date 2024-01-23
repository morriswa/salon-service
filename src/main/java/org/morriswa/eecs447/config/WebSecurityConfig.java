package org.morriswa.eecs447.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-19 <br>
 * PURPOSE: <br>
 * &emsp; will provide all security config for the application
 */

@Configuration
@EnableWebSecurity // Enables Spring Security for this application
public class WebSecurityConfig {

    /**
     * Register a Password Encoder bean to be used throughout the application
     *
     * @return the password encoder implementation
     */
    @Bean @Profile("!test") public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Register an Authentication Manager bean to be used by Spring Security filter
     * to authenticate users' basic auth token
     * <p>
     * DEPENDENCIES:
     * @param userService an implementation of spring's User Detail Service
     * @param passwordEncoder an implementation of Password Encoder
     * @return the final configured application Authentication Manager
     */
    @Autowired @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Register a Security Filter Chain bean to secure all web requests
     *
     * @param http Spring's Http Security object, used for security configuration
     * @return the final configured application Security Filter
     * @throws Exception if the Security Filter cannot be configured for any reason
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http    // All http requests will...
                // Be stateless
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Be authorized only by following below rules
                .authorizeHttpRequests(authorize -> authorize
                        // requests to user registration endpoint shall be allowed
                        .requestMatchers("/register").permitAll()
                        // all other requests must be authenticated
                        .anyRequest().authenticated()
                )
                // use default http basic authorization token, provided in http headers
                .httpBasic(Customizer.withDefaults())
                // not conform to cors or crsf security standards
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}