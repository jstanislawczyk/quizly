package com.quizly.services;

import com.quizly.models.entities.Quiz;
import com.quizly.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;

    public List<Quiz> findQuizzesPageByUserEmail(final String email, final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        return this.quizRepository.findByUserEmailWithLimit(email, pageable).getContent();
    }

    public Optional<Quiz> findQuizByUniqueCode(final String uniqueCode) {
        return this.quizRepository.findQuizByUniqueCode(uniqueCode);
    }

    public Quiz createQuiz(final Quiz quiz) {
        quiz.setStartedAt(LocalDateTime.now());
        quiz.setUniqueCode(UUID.randomUUID().toString());

        return this.saveQuiz(quiz);
    }

    public Quiz saveQuiz(final Quiz quiz) {
        return this.quizRepository.save(quiz);
    }

    public boolean isQuizOutOfDate(final Quiz quiz, final LocalDateTime finishTime) {
        if (quiz.getQuizDurationInSeconds() == 0) {
            return false;
        }

        final LocalDateTime maximumFinishTime = quiz.getStartedAt().plusSeconds(quiz.getQuizDurationInSeconds());

        return finishTime.isAfter(maximumFinishTime);
    }
}
