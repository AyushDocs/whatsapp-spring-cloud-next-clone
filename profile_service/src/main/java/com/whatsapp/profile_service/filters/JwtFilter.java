package com.whatsapp.profile_service.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whatsapp.profile_service.config.JwtConfig;
import com.whatsapp.profile_service.models.CustomUserDetails;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
      private final JwtConfig jwtConfig;
      private final JwtUtils jwtUtils;

      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                  throws ServletException, IOException {
            JwtHandler handler = new JwtHandler(request);
            handler.handleRequest();
            filterChain.doFilter(request, response);
      }

      
      @RequiredArgsConstructor
      private class JwtHandler {
            private final HttpServletRequest request;

            private void handleRequest() {
                  Optional
                              .ofNullable(request.getCookies())
                              .ifPresentOrElse(this::createUserBasedOnCookies,
                               this::createSimpleUser);
            }

            private void createUserBasedOnCookies(Cookie[] cookies) {
                  getJwtCookieFromCookies(cookies)
                              .ifPresentOrElse(this::saveUserInSpringSecurity,
                                          this::createSimpleUser);
            }

            private void saveUserInSpringSecurity(Cookie cookie) {
                  String jwt = cookie.getValue();
                  if (jwtIsValid(jwt)) createJwtUser(jwt);
                  else createSimpleUser();
            }

            private boolean jwtIsValid(String jwt) {
                  return jwtUtils.verifyToken(jwt);
            }

            private Optional<Cookie> getJwtCookieFromCookies(Cookie[] cookies) {
                  return Arrays.stream(cookies)
                              .filter(cookie -> cookie.getName().equals(jwtConfig.getCookieName()))
                              .findFirst();
            }

            private void createSimpleUser() {
                  if (SecurityContextHolder.getContext().getAuthentication() != null)return;
                  List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("NONE"));
                  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null,
                              null, authorities);
                  setUserInContext(authentication);
            }

            private void createJwtUser(String token) {
                  CustomUserDetails user = jwtUtils.extractUser(token);
                  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                              null,
                              user.getAuthorities());
                  setUserInContext(authentication);
            }

            private void setUserInContext(UsernamePasswordAuthenticationToken authentication) {
                  WebAuthenticationDetails webAuthDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                  authentication.setDetails(webAuthDetails);
                  SecurityContextHolder.getContext().setAuthentication(authentication);
            }
      }
}
