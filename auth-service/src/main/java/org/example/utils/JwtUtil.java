package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.dto.JwtDTO;
import org.example.enums.Roles;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String secret = "mySuperSecretKeyThatIsAtLeast32CharsLong1234567890";
    private static final long expiration = 1000 * 60 * 60 * 48; // 2 kun

    public static String encode(Long profileId, String username, Roles role) {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", profileId.toString());
        claims.put("roles", String.valueOf(role));

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static String encode(Long profileId) {
        return Jwts.builder()
                .subject(String.valueOf(profileId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

/*
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }*/

    public static JwtDTO decode(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String idClaim = claims.get("id", String.class);
        String id = (idClaim != null) ? idClaim : claims.getSubject();

        String email = claims.get("username", String.class);

        String strRoles = claims.get("roles", String.class);

        if (strRoles != null) {
            Roles role = Roles.valueOf(strRoles);
            String emailCheck = claims.getSubject();
            return new JwtDTO(id, emailCheck, role);
        }
        return new JwtDTO(id, email);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Long decodeVerRegToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    public Long getProfileId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.get("id", String.class));
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
        return  Keys.hmacShaKeyFor(keyBytes);
    }
}

