package com.quizly.services;

import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Optional<Question> findQuestionById(final Long id) {
        return this.questionRepository.findById(id);
    }

    public Question saveQuestion(final Question question) {
        log.info("Saving new question");

        return this.questionRepository.save(question);
    }

    public void deleteQuestionById(final Long id) {
        this.questionRepository.deleteById(id);
    }
}
