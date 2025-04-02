package edu.ntnu.idatt2105.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up authentication and authorization configurations.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configuration for the filter chain, allowing for configuring endpoint authentication
   * and authorization requirements.
   *
   * @param http the HttpSecurity to modify
   * @return the configured SecurityChainFilter
   * @throws Exception if any error occurs while configuring the filter chain
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(authorizeRequests ->
        authorizeRequests.requestMatchers("/api/store/**").permitAll()
            .requestMatchers("/api/token/**").permitAll()
            .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());
    http.sessionManagement(
        session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
    );
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/token/**"));
    http.addFilterBefore(new JWTAuthorizationFilter(),
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
