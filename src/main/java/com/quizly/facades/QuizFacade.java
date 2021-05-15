package com.quizly.facades;

import com.quizly.enums.QuestionType;
import com.quizly.exceptions.BadRequestException;
import com.quizly.exceptions.NotFoundException;
import com.quizly.models.common.QuestionAnswer;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import com.quizly.models.entities.Quiz;
import com.quizly.services.QuestionService;
import com.quizly.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizFacade {

    private final QuestionService questionService;
    private final QuizService quizService;

    public Quiz createQuiz(final Quiz quiz, final List<QuestionType> types, final int quantity) {
        final List<Question> quizQuestions = this.questionService.getRandomQuestionsList(types, quantity);
        quiz.setQuestions(quizQuestions);
        quiz.setTotalQuestions(quantity);

        return this.quizService.createQuiz(quiz);
    }

    public Quiz finishQuiz(final String uniqueQuizCode, final List<QuestionAnswer> questionAnswers) {
        final LocalDateTime finishTime = LocalDateTime.now();
        final Quiz quiz = this.quizService
                .findQuizByUniqueCode(uniqueQuizCode)
                .orElseThrow(() ->
                    new NotFoundException("Quiz with code=" + uniqueQuizCode + " not found")
                );

        if (this.quizService.isQuizOutOfDate(quiz, finishTime)) {
            throw new BadRequestException("Quiz with code=" + uniqueQuizCode + " is out of date");
        }

        if (false) {
            throw new BadRequestException("Quiz with code=" + uniqueQuizCode + " was already finished");
        }

        final List<Long> questionsIds = quiz.getQuestions()
                .stream()
                .map(Question::getId)
                .collect(Collectors.toList());
        final List<Question> questionsWithCorrectAnswers = this.questionService.findQuestionsByIdsWithCorrectAnswers(questionsIds);

        quiz.setFinishedAt(finishTime);
        this.quizService.saveQuiz(quiz);
        quiz.setQuestions(questionsWithCorrectAnswers);

        return quiz;
    }
}
