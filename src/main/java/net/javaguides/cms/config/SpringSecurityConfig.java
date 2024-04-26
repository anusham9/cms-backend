package net.javaguides.cms.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import net.javaguides.cms.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

/**
 * Configuration class for Spring Security that defines beans and security rules for the application.
 * This class leverages Spring Security to enforce authentication and authorization policies.
 *
 * <p>Features include:
 * <ul>
 *   <li>Disabling CSRF protection to support stateless session management suitable for REST APIs.
 *   <li>Stateless session management to prevent session creation on the server.
 *   <li>Configurable authentication and authorization paths, with some endpoints public and others secured.
 *   <li>Use of HTTP Basic authentication for simplicity.
 * </ul>
 *
 * <p>This configuration ensures that various API endpoints are either restricted to authenticated users or available publicly.
 * Additionally, it configures the application to use bcrypt password encoding for secure password management.
 *
 * @author Annotated with Lombok's {@link AllArgsConstructor} to inject dependencies through constructor.
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 */

@Configuration
@EnableWebSecurity
@AllArgsConstructor

public class SpringSecurityConfig {


  private net.javaguides.cms.security.CustomUserDetailsService userDetailsService;


  /**
   * Bean configuration for the password encoder.
   * Uses BCrypt hashing algorithm for encoding passwords securely.
   *
   * @return A password encoder that uses the BCrypt hashing method.
   */
  @Bean
  public static PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the security filter chain that applies HTTP security configurations.
   *
   * <p>Specific configurations include:
   * <ul>
   *   <li>Disables CSRF to support REST APIs.
   *   <li>Configures access rules for various API endpoints.
   *   <li>Enables HTTP Basic authentication.
   * </ul>
   *
   * @param http the {@link HttpSecurity} to configure.
   * @return the configured {@link SecurityFilterChain}.
   * @throws Exception if an error occurs during configuration.
   */

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf((csrf) -> csrf.disable())
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((authorize) -> {

          authorize.requestMatchers("/cms/employees").hasRole("ADMIN");
          authorize.requestMatchers("/cms/employees/*").hasRole("ADMIN");
          authorize.requestMatchers("/cms/clients").hasAnyRole("EMPLOYEE", "ADMIN");
          authorize.requestMatchers("/cms/clients/*").hasAnyRole("EMPLOYEE", "ADMIN");
          authorize.requestMatchers("/cms/profile/*").hasRole("CLIENT");
          authorize.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());



    return http.build();
  }

  /**
   * Exposes the Spring Security AuthenticationManager as a bean.
   *
   * @param configuration the {@link AuthenticationConfiguration} used to build the AuthenticationManager.
   * @return the AuthenticationManager bean.
   * @throws Exception if there is a problem obtaining the AuthenticationManager.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();

  }
}
