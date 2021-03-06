package com.quizly.services;

import com.quizly.enums.Role;
import com.quizly.exceptions.ConflictException;
import com.quizly.exceptions.NotFoundException;
import com.quizly.exceptions.ValidationException;
import com.quizly.models.common.UserLoginData;
import com.quizly.models.common.UserRegisterData;
import com.quizly.models.entities.User;
import com.quizly.repositories.UserRepository;
import com.quizly.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getLoggedUser(final UserLoginData userLoginData) {
        final User user = this.userRepository
                .findUserByEmail(userLoginData.getEmail())
                .orElseThrow(() ->
                    new NotFoundException("User logging with mail=" + userLoginData.getEmail() + " failed")
                );
        final String rawPassword = String.valueOf(userLoginData.getPassword());
        final String passwordHash = String.valueOf(user.getPassword());

        if (SecurityUtils.checkPasswordEquality(rawPassword, passwordHash, this.passwordEncoder)) {
            return user;
        } else {
            throw new NotFoundException("User logging with mail=" + userLoginData.getEmail() + " failed");
        }
    }

    public User getAuthenticationUser(final Authentication authentication) {
        final Optional<String> userEmail = SecurityUtils.getUserEmail(authentication);

        return userEmail
                .flatMap(this.userRepository::findUserByEmail)
                .orElse(null);
    }

    public User createUser(final UserRegisterData userRegisterData) {
        this.validateRegisterData(userRegisterData);
        this.userRepository
            .findUserByEmail(userRegisterData.getEmail())
            .ifPresent(user -> {
                throw new ConflictException("User with mail=" + userRegisterData.getEmail() + " already exists");
            });
        final char[] encodedPassword = passwordEncoder.encode(CharBuffer.wrap(userRegisterData.getPassword())).toCharArray();
        final User user = User
                .builder()
                    .email(userRegisterData.getEmail())
                    .password(encodedPassword)
                    .role(Role.STUDENT)
                .build();

        return userRepository.save(user);
    }

    private void validateRegisterData(final UserRegisterData userRegisterData) {
        this.validatePasswordRepeat(userRegisterData);
        this.validateUserPassword(userRegisterData.getPassword());
    }

    private void validatePasswordRepeat(final UserRegisterData userRegisterData) {
        if (!Arrays.equals(userRegisterData.getPassword(), userRegisterData.getPasswordRepeat())) {
            throw new ValidationException("Password and repeated password are not equal");
        }
    }

    private void validateUserPassword(final char[] password) {
        final Supplier<Stream<Character>> passwordStreamSupplier = () ->
                IntStream
                        .range(0, password.length)
                        .mapToObj(index -> password[index]);
        final boolean passwordIsToShort = password.length < User.MINIMUM_PASSWORD_LENGTH;
        final boolean passwordNotContainsNumber = passwordStreamSupplier.get().noneMatch(Character::isDigit);
        final boolean passwordNotContainsUppercaseLetter = passwordStreamSupplier.get().noneMatch(Character::isUpperCase);

        if (passwordIsToShort || passwordNotContainsNumber || passwordNotContainsUppercaseLetter) {
            List<String> validationMessages = new ArrayList<>();

            if (passwordIsToShort) {
                validationMessages.add("password is too short");
            }

            if (passwordNotContainsNumber) {
                validationMessages.add("password does not contain a number");
            }

            if (passwordNotContainsUppercaseLetter) {
                validationMessages.add("password does not contain an uppercase letter");
            }

            final String fullErrorMessage = String.join(", ", validationMessages);

            throw new ValidationException(fullErrorMessage);
        }
    }
}
