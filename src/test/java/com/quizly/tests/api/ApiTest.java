package com.quizly.tests.api;

import com.quizly.repositories.AnswerRepository;
import com.quizly.repositories.QuestionRepository;
import com.quizly.repositories.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @BeforeEach
    public void beforeEachTest() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        answerRepository.deleteAll();
        quizRepository.deleteAll();
        questionRepository.deleteAll();
    }
}
