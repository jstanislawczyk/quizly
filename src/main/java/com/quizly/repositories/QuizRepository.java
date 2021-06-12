package com.quizly.repositories;

import com.quizly.models.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @EntityGraph(attributePaths = {"questions"})
    Optional<Quiz> findQuizByUniqueCode(final String uniqueCode);

    @Query(
        value = """
        SELECT quiz from Quiz quiz
        JOIN quiz.user user
        WHERE user.email = :email
        """
    )
    Page findByUserEmailWithLimit(final String email, final Pageable pageable);
}
