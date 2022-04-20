package com.whatsapp.profile_service.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.whatsapp.profile_service.config.JwtConfig;
import com.whatsapp.profile_service.models.CustomUserDetails;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.models.User.UserBuilder;

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
            try {
                  return extractExpiration(token).after(new Date());
            } catch (Exception e) {
                  return false;
            }
      }

      private Map<String, Object> generateClaimsMap(User user) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            claims.put("uuid", user.getUuid());
            claims.put("username", user.getUsername());
            return claims;
      }

      public CustomUserDetails extractUser(String token) {
            UserBuilder userBuilder = User.builder();
            Map<String, Object> claims = extractClaims(token);
            userBuilder.email((String) claims.get("email"));
            userBuilder.uuid((String) claims.get("uuid"));
            userBuilder.username((String) claims.get("username"));
            return new CustomUserDetails(userBuilder.build());
      }
}