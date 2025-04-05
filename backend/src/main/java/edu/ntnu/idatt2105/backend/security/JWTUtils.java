package edu.ntnu.idatt2105.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utils class for JWT based tasks.
 */
@Service
public class JWTUtils {

  private static final String KEY_SECRET = "04035bd7735d40a293ab588b5471d939";
  private static final Duration JWT_VALIDITY = Duration.ofMinutes(5);

  private final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

  /**
   * generates a json web token based on the userID and role parameters.
   *
   * @param userID the subject of the token
   * @param role the authority of the token
   * @return a jwt for the user
   * @throws IllegalArgumentException if parameters are invalid
   */
  public String generateToken(final int userID, final String role)
      throws IllegalArgumentException {
    if (role == null || role.isBlank()) {
      throw new IllegalArgumentException("Token generation call must include UserID and Role");
    }
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(KEY_SECRET);
    return JWT.create()
        .withSubject(String.valueOf(userID))
        .withIssuer("yard")
        .withIssuedAt(now)
        .withExpiresAt(now.plusMillis(JWT_VALIDITY.toMillis()))
        .withClaim("role", role)
        .sign(hmac512);
  }

  /**
   * validates a given token.
   *
   * @param token the jwt to be validated
   * @return the decoded jwt
   * @throws JWTVerificationException if the verification failed
   */
  private DecodedJWT validateToken(final String token) throws JWTVerificationException {
    try {
      final Algorithm hmac512 = Algorithm.HMAC512(KEY_SECRET);
      final JWTVerifier verifier = JWT.require(hmac512).build();
      return verifier.verify(token);
    } catch (final JWTVerificationException e) {
      logger.warn("token is invalid {}", e.getMessage());
      throw e;
    }
  }

  /**
   * validates and retrieves the user id from the given token.
   *
   * @param token the jwt to get user id from
   * @return the user id
   * @throws IllegalArgumentException if token doesn't contain a subject
   */
  public String validateTokenAndGetUserId(final String token) throws IllegalArgumentException {
    String subject = validateToken(token).getSubject();
    if (subject == null) {
      logger.error("Token does not contain a subject");
      throw new IllegalArgumentException("Token does not contain a subject");
    }
    return subject;
  }

  /**
   * validates and retrieves the role from the given token.
   *
   * @param token the jwt to get role from
   * @return the role
   * @throws IllegalArgumentException if token doesn't contain a role
   */
  public String validateTokenAndGetRole(final String token) throws IllegalArgumentException {
    String role = validateToken(token).getClaim("role").asString();
    if (role == null) {
      logger.error("Token does not contain a role");
      throw new IllegalArgumentException("Token does not contain a role");
    }
    return role;
  }


  /**
   * Sets a JWT token as an HTTP-only, secure cookie in the response.
   *
   * @param jwtToken The JWT token to be set in the cookie.
   * @param response The HttpServletResponse object to which the cookie will be added.
   */
  public void setJWTCookie(String jwtToken, HttpServletResponse response) {
    Cookie jwtCookie = new Cookie("JWT", jwtToken);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge((int) JWT_VALIDITY.getSeconds());
    response.addCookie(jwtCookie);
  }
}
