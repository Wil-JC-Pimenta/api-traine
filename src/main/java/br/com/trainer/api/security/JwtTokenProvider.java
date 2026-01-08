package br.com.trainer.api.security;

import br.com.trainer.api.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final UserDetailsServiceImpl userDetailsService;
    private SecretKey secretKey;

    public JwtTokenProvider(JwtConfig jwtConfig, UserDetailsServiceImpl userDetailsService) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        // Build the signing key from the configured secret. For production we require a sufficiently
        // long secret (>= 32 bytes after UTF-8 or after Base64 decode). If it's not secure enough
        // we fail fast so deployments don't start with weak keys.
        String secret = jwtConfig.getSecret();
        if (secret == null) {
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
