package com.quizly.facades;

import com.quizly.enums.QuestionType;
import com.quizly.exceptions.BadRequestException;
import com.quizly.exceptions.NotFoundException;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import com.quizly.services.AnswerService;
import com.quizly.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionFacade {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public List<Question> getQuestionsList(final List<QuestionType> types, final int quantity) {
        return this.questionService.getRandomQuestionsList(types, quantity);
    }

    public Question saveQuestionWithAnswers(final Question question, final List<Answer> answers) {
        this.validateAnswers(question, answers);
        final Question savedQuestion = this.questionService.saveQuestion(question);
        answers.forEach(answer -> answer.setQuestion(savedQuestion));
        final List<Answer> savedAnswers = this.answerService.saveAnswers(answers);
        savedQuestion.setAnswers(savedAnswers);

        return savedQuestion;
    }

    public void deleteQuestionById(final Long id) {
        this.questionService
                .findQuestionById(id)
                .orElseThrow(() ->
                    new NotFoundException("Question with id=" + id + " not found")
                );
        this.questionService.deleteQuestionById(id);
    }

    private void validateAnswers(final Question question, final List<Answer> answers) {
        switch (question.getQuestionType()) {
            case SINGLE_CHOICE -> {
                this.validateSingleChoiceQuestion(answers);
            }
            case MULTI_CHOICE -> {
                this.validateMultiChoiceQuestion(answers);
            }
            case OPEN -> {
            }
        }
    }

    private void validateSingleChoiceQuestion(final List<Answer> answers) {
        final List<Answer> correctAnswers = answers
                .stream()
                .filter(Answer::isCorrect)
                .collect(Collectors.toList());

        if (correctAnswers.size() != 1) {
            throw new BadRequestException("Single choice question must contain one correct answer");
        }
    }

    private void validateMultiChoiceQuestion(final List<Answer> answers) {
        final boolean hasNoCorrectAnswer = answers
                .stream()
                .noneMatch(Answer::isCorrect);

        if (hasNoCorrectAnswer) {
            throw new BadRequestException("Multi choice question must contain at least one correct answer");
        }
    }
}
