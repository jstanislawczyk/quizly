package com.quizly.tests.api;

import com.quizly.repositories.AnswerRepository;
import com.quizly.repositories.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @AfterEach
    public void afterEachTest() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
    }
}
