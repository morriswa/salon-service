package org.morriswa.salon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.morriswa.salon.utility.ServiceInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * will provide all security config for the application
 *
 * @author William A. Morris
 * @since 2024-01-19
 */

@Configuration
@EnableWebSecurity // Enables Spring Security for this application
public class WebSecurityConfig {

    /**
     * Register a Password Encoder bean to be injected
     * as a dependency throughout the application
     *
     * @return the password encoder implementation
     */
    @Bean @Profile("!test")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Register an Authentication Manager bean to be used by Spring Security filter
     * to authenticate users' basic auth token
     * <p>
     * REQUIRED AUTOWIRED DEPENDENCIES:
     * @param userService an implementation of spring's User Detail Service
     *                    for this application org.morriswa.salon.utility.JdbcAuthenticationService
     * @param passwordEncoder an implementation of Password Encoder
     * @return the final configured application Authentication Manager
     */
    @Bean @Autowired
    public AuthenticationManager authenticationManager(UserDetailsService userService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Returns all Cors (Cross Origin Resource Sharing) Configuration for the application
     *
     * @return fully configured Cors Object, ready to be injected into Security Filter
     */
    private static CorsConfigurationSource corsConfigurationSource() {

        // configuration for Secured Routes
        final CorsConfiguration secureRoutesCors = new CorsConfiguration(){{
            // allow requests coming from any origin
            setAllowedOrigins(List.of("*"));
            // allow only GET and POST HTTP methods
            setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
            // allow request to have any headers
            setAllowedHeaders(List.of("*"));
        }};

        // configuration for Public Routes
        final CorsConfiguration publicEndpointCors = new CorsConfiguration(){{
            // allow requests coming from any origin
            setAllowedOrigins(List.of("*"));
            // only allow GET and POST methods
            setAllowedMethods(List.of("GET", "POST"));
            // allow request to have any headers
            setAllowedHeaders(List.of("*"));
        }};

        // cors configuration will vary based on url
        UrlBasedCorsConfigurationSource sources = new UrlBasedCorsConfigurationSource();

        // register all routes with secured cors config
        sources.registerCorsConfiguration("/**", secureRoutesCors);

        // register user registration route with appropriate config
        sources.registerCorsConfiguration("/register", publicEndpointCors);

        // register user registration route with appropriate config
        sources.registerCorsConfiguration("/health", publicEndpointCors);

        // return fully configured cors source
        return sources;
    }

    /**
     * Register a Security Filter Chain bean to secure all web requests
     * REQUIRED AUTOWIRED DEPENDENCIES:
     * @param http Spring's Http Security object, used for security configuration
     * @param responseFactory Used to generate formatted error responses for HTTP consumption
     * @return the final configured application Security Filter
     * @throws Exception if the Security Filter cannot be configured for any reason
     */
    @Bean @Autowired
    public SecurityFilterChain configure(HttpSecurity http,
                                         ServiceInfoFactory responseFactory,
                                         ObjectMapper objectMapper) throws Exception {

        http    // All http requests will...
                // Be stateless
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Be authorized only by following below rules
                .authorizeHttpRequests(authorize -> authorize
                        // requests to user registration endpoint shall be allowed
                        .requestMatchers("/register", "/health", "/public/**").permitAll()
                        // only new user accounts should have access to account registration endpoints
                        .requestMatchers("/newUser/**").hasAuthority("NUSER")
                        // only authenticated accounts can access login endpoint
                        .requestMatchers("/login").hasAnyAuthority("NUSER", "USER")
                        // only complete user accounts can access user management endpoints
                        .requestMatchers("/user/**").hasAuthority("USER")
                        // only employees can access biz management endpoints
                        .requestMatchers("/employee/**").hasAuthority("EMPLOYEE")
                        // only clients can access client portal info
                        .requestMatchers("/client/**").hasAuthority("CLIENT")
                        // clients and employees have accessed to shared endpoints
                        .requestMatchers("/shared/**").hasAnyAuthority("CLIENT","EMPLOYEE")
                        // employees and clients may have access to remaining endpoints
                        .anyRequest().denyAll()
                )
                // disable cross site protections
                .csrf(csrf->csrf.disable())
                // use custom cors config
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                // use default http basic authorization token, provided in http headers
                // and register exception handler for requests with bad credentials (401)
                .httpBasic(basic->basic.authenticationEntryPoint((request, response, authException) -> {
                    // create a formatted Http Response
                    var customErrorResponse =
                        responseFactory.getHttpErrorResponse(
                            // status should be 401
                            HttpStatus.UNAUTHORIZED,
                            // error should be the exception encountered in the filter chain
                            authException.getClass().getSimpleName(),
                            // error description to include in response
                            """
                            Could not authenticate with provided credentials. \
                            If you believe this is a mistake, check your login information."""
                        );

                    // write the body of the generated Http Response to actual Response
                    response.getOutputStream().println(
                            objectMapper.writeValueAsString(customErrorResponse.getBody()));
                    // content type of response will be json
                    response.setContentType("application/json");
                    // status of response should be 401
                    response.setStatus(customErrorResponse.getStatusCode().value());
                    // continue response chain
                }))
                // register exception handler for requests without authentication (401)
                .exceptionHandling(exceptions->exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    // create a formatted Http Response
                    var customErrorResponse =
                        responseFactory.getHttpErrorResponse(
                            // status should be 401
                            HttpStatus.UNAUTHORIZED,
                            // error should be the exception encountered in the filter chain
                            authException.getClass().getSimpleName(),
                            // error description to include in response
                            """ 
                            YOU SHALL NOT PASS! \
                            This endpoint requires authentication, which you did not bother to provide..."""
                        );

                    // write the body of the generated Http Response to actual Response
                    response.getOutputStream().println(
                            objectMapper.writeValueAsString(customErrorResponse.getBody()));
                    // content type of response will be json
                    response.setContentType("application/json");
                    // status of response should be 401
                    response.setStatus(customErrorResponse.getStatusCode().value());
                    // continue response chain
                })
                // register exception handler for requests without proper scope (403)
                .accessDeniedHandler((request, response, authException) -> {
                    // create a formatted Http Response
                    var customErrorResponse =
                        responseFactory.getHttpErrorResponse(
                            // status should be 401
                            HttpStatus.FORBIDDEN,
                            // error should be the exception encountered in the filter chain
                            authException.getClass().getSimpleName(),
                            // error description to include in response
                            """ 
                            YOU SHALL NOT PASS! \
                            You do not have permission to access this endpoint. \
                            If you believe this is a mistake, please contact your system administrator."""
                        );

                    // write the body of the generated Http Response to actual Response
                    response.getOutputStream().println(
                            objectMapper.writeValueAsString(customErrorResponse.getBody()));
                    // content type of response will be json
                    response.setContentType("application/json");
                    // status of response should be 401
                    response.setStatus(customErrorResponse.getStatusCode().value());
                    // continue response chain
                }));

        // build http security object, and return if no errors are encountered
        return http.build();
    }
}