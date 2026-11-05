package rayyan.warehouse.workflowautomationengine.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

    private final SecretKey signingKey;
    private final long expirationMS;

    public JWTService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMS
    ) {
        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );

        this.expirationMS = expirationMS;
    }

    public String generateToken(UUID uuid) {

        Date issuedAt = new Date();
        Date expiresAt = new Date(
                issuedAt.getTime() + expirationMS
        );

        return Jwts.builder()
                .subject(uuid.toString())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    // Extract UUID from JWT subject
    public UUID extractUserId(String token) {

        String subject = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return UUID.fromString(subject);
    }

    // Validate JWT against expected user ID
    public boolean isTokenValid(String token, UUID userId) {

        UUID tokenUserId = extractUserId(token);

        return tokenUserId.equals(userId);
    }
}
