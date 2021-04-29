package com.quizly.facades;

import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import com.quizly.services.AnswerService;
import com.quizly.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionFacade {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public Question saveQuestionWithAnswers(final Question question, final List<Answer> answers) {
        final Question savedQuestion = this.questionService.saveQuestion(question);
        answers.forEach(answer -> answer.setQuestion(savedQuestion));
        final List<Answer> savedAnswers = this.answerService.saveAnswers(answers);
        savedQuestion.setAnswers(savedAnswers);

        return savedQuestion;
    }
}
