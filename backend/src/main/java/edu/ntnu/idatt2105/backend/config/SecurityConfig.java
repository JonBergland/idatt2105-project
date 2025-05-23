package edu.ntnu.idatt2105.backend.config;

import edu.ntnu.idatt2105.backend.security.JWTAuthorizationFilter;
import edu.ntnu.idatt2105.backend.utils.JWTUtils;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.setAllowedHeaders(List.of("*"));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);

    http.cors(cors -> cors.configurationSource(source))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/api/token/signup",
                "/api/token/signin",
                "/api/store/**",
                "/swagger-ui/**",
                "/v3/api-docs/**")
            .permitAll()
            .requestMatchers("/api/user/**")
            .hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/admin/**")
            .hasRole("ADMIN")
            .anyRequest().authenticated())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            
    http.addFilterBefore(
        new JWTAuthorizationFilter(new JWTUtils()), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

  /**
   * Bean for password encoder using bcrypt encoding.
   *
   * @return the bcrypt passwordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
