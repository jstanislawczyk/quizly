package com.quizly.facades;

import com.quizly.enums.QuestionType;
import com.quizly.exceptions.BadRequestException;
import com.quizly.exceptions.NotFoundException;
import com.quizly.models.common.QuestionAnswer;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import com.quizly.models.entities.Quiz;
import com.quizly.models.entities.User;
import com.quizly.services.QuestionService;
import com.quizly.services.QuizService;
import com.quizly.services.UserService;
import com.quizly.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizFacade {

    private final QuestionService questionService;
    private final QuizService quizService;
    private final UserService userService;

    public List<Quiz> getQuizzesPageByForUser(final Authentication authentication, final int page, final int size) {
        final String email = SecurityUtils
                .getUserEmail(authentication)
                .orElseThrow(() ->
                    new NotFoundException("User not authenticated")
                );

        return this.quizService.findQuizzesPageByUserEmail(email, page, size);
    }

    public Quiz getUserQuizByCodeForUser(final Authentication authentication, final String code) {
        final String email = SecurityUtils
                .getUserEmail(authentication)
                .orElseThrow(() ->
                    new NotFoundException("User not authenticated")
                );

        return this.quizService.findQuizByCodeAndUserEmail(email, code);
    }

    public Quiz createQuiz(
        final Quiz quiz, final List<QuestionType> types, final int quantity, final Authentication authentication
    ) {
        final List<Question> quizQuestions = this.questionService.getRandomQuestionsList(types, quantity);
        final User quizUser = this.userService.getAuthenticationUser(authentication);

        quiz.setQuestions(quizQuestions);
        quiz.setTotalQuestions(quantity);
        quiz.setUser(quizUser);

        return this.quizService.createQuiz(quiz);
    }

    public Quiz finishQuiz(final String uniqueQuizCode, final List<QuestionAnswer> questionAnswers) {
        final LocalDateTime finishTime = LocalDateTime.now();
        final Quiz quiz = this.quizService
                .findQuizByUniqueCode(uniqueQuizCode)
                .orElseThrow(() ->
                    new NotFoundException("Quiz with code=" + uniqueQuizCode + " not found")
                );

        if (quiz.getFinishedAt() != null) {
            throw new BadRequestException("Quiz with code=" + uniqueQuizCode + " was already finished");
        }

        if (this.quizService.isQuizOutOfDate(quiz, finishTime)) {
            throw new BadRequestException("Quiz with code=" + uniqueQuizCode + " is out of date");
        }

        final List<Question> questionsWithCorrectAnswers = this.questionService.getQuestionsByIdsWithCorrectAnswers(quiz.getQuestions());
        final List<QuestionAnswer> updatedQuestionAnswers = this.updateQuestionAnswersWithQuestionData(questionAnswers, questionsWithCorrectAnswers);
        final List<QuestionAnswer> correctQuestionAnswers = updatedQuestionAnswers
                .stream()
                .filter(QuestionAnswer::isCorrect)
                .collect(Collectors.toList());

        quiz.setFinishedAt(finishTime);
        quiz.setQuestions(questionsWithCorrectAnswers);
        quiz.setTotalQuestions(quiz.getQuestions().size());
        quiz.setCorrectQuestions(correctQuestionAnswers.size());
        quiz.setGainedPoints(this.countGainedPoints(correctQuestionAnswers));
        quiz.setTotalPoints(this.countTotalPoints(questionsWithCorrectAnswers));

        this.quizService.saveQuiz(quiz);

        return quiz;
    }

    private List<QuestionAnswer> updateQuestionAnswersWithQuestionData(
        final List<QuestionAnswer> questionAnswers,
        final List<Question> questionsWithCorrectAnswers
    ) {
        return questionAnswers
            .stream()
            .map(questionAnswer -> {
                final Question questionForAnswer = this.questionService.findQuestionByIdInList(questionAnswer.getQuestionId(), questionsWithCorrectAnswers);
                final List<Character> correctAnswerPoints = this.getCorrectAnswerPoints(questionForAnswer);

                questionAnswer.setCorrectAnswerOptions(correctAnswerPoints);
                questionAnswer.setCorrect(CollectionUtils.isEqualCollection(questionAnswer.getAnswerOptions(), correctAnswerPoints));
                questionAnswer.setPoints(this.getGainedPoints(questionAnswer, questionForAnswer));
                questionAnswer.setQuestionType(questionForAnswer.getQuestionType());

                return questionAnswer;
            })
            .collect(Collectors.toList());
    }

    private List<Character> getCorrectAnswerPoints(final Question question) {
        return question
                .getAnswers()
                .stream()
                .map(Answer::getAnswerOption)
                .collect(Collectors.toList());
    }

    private int getGainedPoints(final QuestionAnswer questionAnswer, final Question question) {
        if (question.getQuestionType().equals(QuestionType.OPEN)) {
            return 0;
        }

        return questionAnswer.isCorrect()
            ? question.getPoints()
            : 0;
    }

    private int countGainedPoints(final List<QuestionAnswer> correctQuestionAnswers) {
        return correctQuestionAnswers
                .stream()
                .mapToInt(QuestionAnswer::getPoints)
                .sum();
    }

    private int countTotalPoints(final List<Question> questions) {
        return questions
                .stream()
                .mapToInt(Question::getPoints)
                .sum();
    }
}
