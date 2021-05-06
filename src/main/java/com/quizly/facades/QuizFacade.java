package com.quizly.facades;

import com.quizly.enums.QuestionType;
import com.quizly.models.entities.Question;
import com.quizly.models.entities.Quiz;
import com.quizly.services.QuestionService;
import com.quizly.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizFacade {

    private final QuestionService questionService;
    private final QuizService quizService;

    public Quiz createQuiz(final Quiz quiz, final List<QuestionType> types, final int quantity) {
        final List<Question> quizQuestions = this.questionService.getRandomQuestionsList(types, quantity);

        return this.quizService.saveQuiz(quiz);
    }
}
