package com.quizly.services;

import com.quizly.models.entities.Answer;
import com.quizly.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {

    private final AnswerRepository answerRepository;

    public List<Answer> saveAnswers(final List<Answer> answers) {
        log.info("Saving new answers");

        return this.answerRepository.saveAll(answers);
    }
}
