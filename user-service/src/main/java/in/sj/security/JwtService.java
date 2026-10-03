package in.sj.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(
            @Value("${app.jwt.secret}") String secret) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(
            Long userId,
            String username,
            String role) {

        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + 60 * 60 * 1000
        );

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}