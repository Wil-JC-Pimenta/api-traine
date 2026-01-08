package br.com.trainer.api.security;

import br.com.trainer.api.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtConfig jwtConfig;
    private final UserDetailsServiceImpl userDetailsService;
    private final Environment env;
    private SecretKey secretKey;

    public JwtTokenProvider(JwtConfig jwtConfig, UserDetailsServiceImpl userDetailsService, Environment env) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.env = env;
    }

    @PostConstruct
    public void init() {
        // Build the signing key from the configured secret. For production we require a sufficiently
        // long secret (>= 32 bytes after UTF-8 or after Base64 decode). If it's not secure enough
        // we fail fast so deployments don't start with weak keys. For the 'test' profile only we
        // allow a secure ephemeral key to be generated so the test suite can run in CI/local
        // without requiring external secret injection.
        String secret = jwtConfig.getSecret();
        boolean isTestProfile = Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.equalsIgnoreCase("test"));

        if (secret == null) {
            if (isTestProfile) {
                // generate a secure ephemeral key for tests (not persisted)
                byte[] generated = new byte[64];
                new SecureRandom().nextBytes(generated);
                secretKey = Keys.hmacShaKeyFor(generated);
                log.warn("No jwt.secret configured and 'test' profile active: generated ephemeral key for tests. Do NOT use this in production.");
                return;
            }
            throw new IllegalStateException("JWT secret must be configured (jwt.secret)");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            try {
                keyBytes = java.util.Base64.getDecoder().decode(secret);
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (keyBytes.length < 32) {
            if (isTestProfile) {
                byte[] generated = new byte[64];
                new SecureRandom().nextBytes(generated);
                secretKey = Keys.hmacShaKeyFor(generated);
                log.warn("Configured jwt.secret is too short or invalid and 'test' profile active: generated ephemeral key for tests. Do NOT use this in production.");
                return;
            }
            throw new IllegalStateException("Configured jwt.secret is too short or invalid. Provide a secure key (e.g. 256-bit base64).");
        }

        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtConfig.getExpirationTime());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
