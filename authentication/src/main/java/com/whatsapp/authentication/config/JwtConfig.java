package com.whatsapp.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
      private long timeDelta;
      @Value("${jwt.secret_key}")
      private String secretKey;
      private String cookieName;
}