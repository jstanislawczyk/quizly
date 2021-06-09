package com.quizly.repositories;

import com.quizly.models.entities.Quiz;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @EntityGraph(attributePaths = {"questions"})
    Optional<Quiz> findQuizByUniqueCode(final String uniqueCode);
}
