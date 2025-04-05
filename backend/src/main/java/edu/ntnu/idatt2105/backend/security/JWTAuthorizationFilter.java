package edu.ntnu.idatt2105.backend.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The JWT filter for authenticating a JWT authenticated request.
 */
@RequiredArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

  private final JWTUtils jwtUtils;


  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    logger.info("JWTAuthorizationFilter called for URI: {}", request.getRequestURI());

    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = "";
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("JWT")) {
        token = cookie.getValue();
        break;
      }
      logger.info(cookie.getName());
    }
    if (token.isBlank()) {
      logger.warn("Token not found");
      filterChain.doFilter(request, response);
      return;
    }

    final String username;
    final String role;
    logger.info("Validating token");
    try {
      username = jwtUtils.validateTokenAndGetUserId(token);
      role = jwtUtils.validateTokenAndGetRole(token);
    } catch (IllegalArgumentException | JWTVerificationException e) {
      filterChain.doFilter(request, response);
      return;
    }

    if (username == null || role == null) {
      filterChain.doFilter(request, response);
      return;
    }

    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority(role));
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        username,
        null,
        authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);

    logger.info("user: {}, role: {}, has been authenticated", username, role);
    filterChain.doFilter(request, response);
  }
}
