package com.quizly.services;

import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        log.info("Saving new question");

        return this.questionRepository.save(question);
    }
}
