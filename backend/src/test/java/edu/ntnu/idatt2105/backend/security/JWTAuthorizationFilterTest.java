package edu.ntnu.idatt2105.backend.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import edu.ntnu.idatt2105.backend.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    Cookie jwtCookie = new Cookie("JWT", VALID_TOKEN);
    when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
    when(jwtUtils.validateTokenAndGetUserId(VALID_TOKEN)).thenReturn(USER_ID);
    when(jwtUtils.validateTokenAndGetRole(VALID_TOKEN)).thenReturn(ROLE);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_ShouldNotAuthenticate_WhenTokenIsInvalid() throws ServletException, IOException {
    Cookie jwtCookie = new Cookie("JWT", VALID_TOKEN);
    when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
    when(jwtUtils.validateTokenAndGetUserId(VALID_TOKEN)).thenReturn(USER_ID);
    when(jwtUtils.validateTokenAndGetRole(VALID_TOKEN)).thenReturn(ROLE);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_ShouldNotAuthenticate_WhenNoTokenProvided() throws ServletException, IOException {
    Cookie jwtCookie = new Cookie("JWT", VALID_TOKEN);
    when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
    when(jwtUtils.validateTokenAndGetUserId(VALID_TOKEN)).thenReturn(USER_ID);
    when(jwtUtils.validateTokenAndGetRole(VALID_TOKEN)).thenReturn(ROLE);

    jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
  }
}
