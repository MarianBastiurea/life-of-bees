package com.marianbastiurea.lifeofbees.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String userId) {
        System.out.println("Generare token pentru userId: " + userId);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        System.out.println("Cheia secretă utilizată in createToken: " + secretKey);

        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("Cheia secretă nu este configurată corect!");
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) {
            System.out.println("Tokenul este null sau gol. Validarea a eșuat.");
            return false;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            boolean isExpired = isTokenExpired(token);
            if (isExpired) {
                System.out.println("Tokenul este expirat.");
                return false;
            }
            return true;

        } catch (JwtException e) {
            System.out.println("Eroare la validarea token-ului JWT: " + e.getMessage());
            System.out.println("Stack Trace: ");
            e.printStackTrace();
            return false;

        } catch (IllegalArgumentException e) {
            System.out.println("Argument invalid la validarea token-ului: " + e.getMessage());
            System.out.println("Stack Trace: ");
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.out.println("Eroare neașteptată la validarea token-ului: " + e.getMessage());
            System.out.println("Stack Trace: ");
            e.printStackTrace();
            return false;
        }
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
