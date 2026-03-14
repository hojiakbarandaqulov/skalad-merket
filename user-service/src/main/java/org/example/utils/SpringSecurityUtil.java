package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
public class SpringSecurityUtil {

   /* public static CustomUserDetails getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user;
    }*/

   /* public static Long getProfileId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails user) {
            return user.getProfile().getUserId();
        }
        return null;
    }
*/
    public static Long getProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            log.info("Token claims: {}", jwt.getClaims());
            return jwt.getClaim("profileId"); // ← endi tokendan olish mumkin
        }
        return null;
    }
  /*  public static String getKeycloakUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return jwt.getSubject(); // Keycloak user UUID (sub claim)
        }
        return null;
    }*/

   /* public static Long getId() {
        CustomUserDetails user = getCurrentProfile();
        return user.getProfile().getUserId();
    }*/
}
