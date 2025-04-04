package edu.ntnu.idatt2105.backend.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {

  @Mock
  private JWTUtils jwtUtils;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private JWTAuthorizationFilter jwtAuthorizationFilter;

  private static final String VALID_TOKEN = "valid.jwt.token";
  private static final String INVALID_TOKEN = "invalid.jwt.token";
  private static final String USER_ID = "123";
  private static final String ROLE = "ROLE_USER";

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void doFilterInternal_ShouldAuthenticate_WhenTokenIsValid() throws ServletException, IOException {
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + VALID_TOKEN);
    when(jwtUtils.validateTokenAndGetUserId(VALID_TOKEN)).thenReturn(USER_ID);
    when(jwtUtils.validateTokenAndGetRole(VALID_TOKEN)).thenReturn(ROLE);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_ShouldNotAuthenticate_WhenTokenIsInvalid() throws ServletException, IOException {
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + INVALID_TOKEN);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_ShouldNotAuthenticate_WhenNoTokenProvided() throws ServletException, IOException {
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_ShouldNotAuthenticate_WhenTokenHasWrongFormat() throws ServletException, IOException {
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("WrongFormatToken");

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }
}
