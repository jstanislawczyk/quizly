package com.quizly.hooks;

import com.quizly.enums.Role;
import com.quizly.models.entities.User;
import com.quizly.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.nio.CharBuffer;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialAdminAccountHook {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createFirstAdminAccount() {
        final boolean databaseHasNoUsers = userRepository.count() == 0;

        if (databaseHasNoUsers) {
            final User admin = this.buildAdminUser();

            userRepository.save(admin);

            log.info("Initial super admin account created");
        }
    }

    private User buildAdminUser() {
        final char[] encodedPassword = passwordEncoder.encode(CharBuffer.wrap("1qazXSW@")).toCharArray();

        return User
                .builder()
                    .email("quizly_admin@gmail.com")
                    .password(encodedPassword)
                    .role(Role.ADMIN)
                    .firstName("Quizly")
                    .lastName("Admin")
                .build();
    }
}
