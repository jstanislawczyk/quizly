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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<QuizDto> getUserQuizzes(
        @RequestParam(defaultValue = "0") @Min(0) final int page,
        @RequestParam(defaultValue = "10") @Min(1) final int size,
        final Authentication authentication
    ) {
        log.info("Fetching quizzes page {} with size {}", page, size);

        final List<Quiz> quizzes = this.quizFacade.getQuizzesPageByForUser(authentication, page, size);

        return this.quizDtoMapper.toDtoList(quizzes);
    }

    @GetMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    public QuizDto getUserQuizByCode(
        @PathVariable final String code,
        final Authentication authentication
    ) {
        log.info("Fetching quiz by code=" + code);

        final Quiz quiz = this.quizFacade.getUserQuizByCodeForUser(authentication, code);

        return this.quizDtoMapper.toDtoWithQuestionsWithAnswers(quiz);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public QuizDto createQuiz(
        @RequestBody @Valid final QuizDto quizDto,
        @RequestParam(defaultValue = "SINGLE_CHOICE") final List<QuestionType> types,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) final int quantity,
        final Authentication authentication
    ) {
        log.info("Preparing quiz with {} questions and types {}", quantity, types);

        final Quiz quiz = this.quizDtoMapper.toEntity(quizDto);
        final Quiz savedQuiz = this.quizFacade.createQuiz(quiz, types, quantity, authentication);

        return this.quizDtoMapper.toDtoWithQuestionsWithoutCorrect(savedQuiz);
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
