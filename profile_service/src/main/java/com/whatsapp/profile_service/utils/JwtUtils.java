package com.whatsapp.profile_service.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import com.whatsapp.profile_service.configuration.JwtConfig;
import com.whatsapp.profile_service.models.CustomUserDetails;
import com.whatsapp.profile_service.models.User;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtConfig jwtConfig;

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setClaims(generateClaimsMap(user))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getTimeDelta()))
                .signWith(SignatureAlgorithm.HS256,
                        jwtConfig.getSecretKey())
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Object extractClaim(String token, Function<Claims, Object> f) {
        return f.apply(extractClaims(token));
    }

    private Date extractExpiration(String token) {
        return (Date) extractClaim(token, Claims::getExpiration);
    }

    public boolean verifyToken(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Map<String, Object> generateClaimsMap(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getName());
        return claims;
    }

    public CustomUserDetails extractUser(String token) {
        User user = new User();
        Map<String, Object> claims = extractClaims(token);
        user.setEmail((String) claims.get("email"));
        user.setName((String) claims.get("username"));
        return new CustomUserDetails(user);
    }
    @PostConstruct
    public void init() {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("\n\n\n\n\n\n"+jwtConfig);
    }
}
