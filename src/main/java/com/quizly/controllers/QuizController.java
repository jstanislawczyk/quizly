package com.quizly.controllers;

import com.quizly.enums.QuestionType;
import com.quizly.facades.QuizFacade;
import com.quizly.mappers.QuestionAnswerMapper;
import com.quizly.mappers.QuizDtoMapper;
import com.quizly.models.common.QuestionAnswer;
import com.quizly.models.dtos.QuestionAnswerDto;
import com.quizly.models.dtos.QuizDto;
import com.quizly.models.entities.Quiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/quiz")
@Validated
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizFacade quizFacade;
    private final QuizDtoMapper quizDtoMapper;
    private final QuestionAnswerMapper questionAnswerMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public QuizDto createQuiz(
        @RequestBody @Valid final QuizDto quizDto,
        @RequestParam(defaultValue = "SINGLE_CHOICE") final List<QuestionType> types,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) final int quantity
    ) {
        log.info("Preparing quiz with {} questions and types {}", quantity, types);

        final Quiz quiz = this.quizDtoMapper.toEntity(quizDto);
        final Quiz savedQuiz = this.quizFacade.createQuiz(quiz, types, quantity);

        return this.quizDtoMapper.toDtoWithQuestions(savedQuiz);
    }

    @PatchMapping("/{uniqueQuizCode}")
    public QuizDto finishQuiz(
        @PathVariable final String uniqueQuizCode,
        @RequestBody @Valid final List<QuestionAnswerDto> questionAnswerDtos
    ) {
        log.info("Finishing quiz with code=" + uniqueQuizCode);

        final List<QuestionAnswer> questionAnswers = this.questionAnswerMapper.toModelList(questionAnswerDtos);
        final Quiz finishedQuiz = this.quizFacade.finishQuiz(uniqueQuizCode, questionAnswers);

        return this.quizDtoMapper.toDtoWithQuestionsWithAnswers(finishedQuiz);
    }
}
