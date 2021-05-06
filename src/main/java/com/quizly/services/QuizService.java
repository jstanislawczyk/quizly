package com.quizly.services;

import com.quizly.models.entities.Quiz;
import com.quizly.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz saveQuiz(final Quiz quiz) {
        quiz.setStartedAt(LocalDateTime.now());
        quiz.setUniqueCode(UUID.randomUUID().toString());

        return this.quizRepository.save(quiz);
    }
}
