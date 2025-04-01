package edu.ntnu.idatt2105.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JWTUtils {

  private static final String KEY_SECRET = "04035bd7735d40a293ab588b5471d939";
  private static final Duration JWT_VALIDITY = Duration.ofMinutes(5);

  private final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

  private DecodedJWT validateToken(final String token) throws JWTVerificationException {
    try {
      final Algorithm hmac512 = Algorithm.HMAC512(KEY_SECRET);;
      final JWTVerifier verifier = JWT.require(hmac512).build();
      return verifier.verify(token);
    } catch (final JWTVerificationException e) {
      logger.warn("token is invalid {}", e.getMessage());
      throw e;
    }
  }

  public String validateTokenAndGetUserId(final String token) throws IllegalArgumentException {
    String subject = validateToken(token).getSubject();
    if (subject == null) {
      logger.error("Token does not contain a subject");
      throw new IllegalArgumentException("Token does not contain a subject");
    }
    return subject;
  }

  public String validateTokenAndGetRole(final String token) throws IllegalArgumentException {
    String role = validateToken(token).getClaim("role").asString();
    if (role == null) {
      logger.error("Token does not contain a role");
      throw new IllegalArgumentException("Token does not contain a role");
    }
    return role;
  }
}
