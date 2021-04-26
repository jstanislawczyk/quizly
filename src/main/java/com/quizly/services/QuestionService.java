package com.quizly.services;

import com.quizly.models.entities.Question;
import com.quizly.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        return this.questionRepository.save(question);
    }
}
