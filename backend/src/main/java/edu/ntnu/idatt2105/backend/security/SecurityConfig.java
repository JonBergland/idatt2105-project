package edu.ntnu.idatt2105.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

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
//    http.addFilterBefore(new JWTAuthorizationFilter(),
//        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
