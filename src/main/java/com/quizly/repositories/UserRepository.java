package com.quizly.repositories;

import com.quizly.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(final String email);
    Optional<User> findUserByEmailAndPassword(final String email, final char[] password);
}
