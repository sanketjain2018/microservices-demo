package in.sj.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTestGenerator {

    public static void main(String[] args) {

        String secret =
                "ThisIsASecretKeyForDevelopmentOnly123456789";

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        String token = Jwts.builder()
                .subject("101")
                .claim("role", "USER")
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 3600000)
                )
                .signWith(key)
                .compact();

        System.out.println(token);
    }
}