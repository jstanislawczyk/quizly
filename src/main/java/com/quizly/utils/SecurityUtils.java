package com.quizly.utils;

import com.quizly.enums.Role;
import com.quizly.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class SecurityUtils {

    public static boolean userHasAuthority(final Authentication authentication, final Role userRole) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority ->
                    authority.equals(userRole.toString())
                );
    }

    public static boolean isCurrentUserAction(final Authentication authentication, final User user) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return user.getEmail().equals(userDetails.getUsername());
    }

    public static Optional<String> getUserEmail(final Authentication authentication) {
        if (authentication == null) {
            return Optional.empty();
        }

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Optional.of(userDetails.getUsername());
    }

    public static boolean checkPasswordEquality(String rawPassword, String passwordHash, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}
