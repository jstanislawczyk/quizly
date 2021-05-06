package com.quizly.controllers;

import com.quizly.enums.QuestionType;
import com.quizly.facades.QuizFacade;
import com.quizly.mappers.QuizDtoMapper;
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

    @PostMapping
    @ResponseStatus(CREATED)
    public QuizDto createQuiz(
        @RequestBody @Valid final QuizDto quizDto,
        @RequestParam(defaultValue = "SINGLE_CHOICE") final List<QuestionType> types,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) final int quantity
    ) {
        log.info("Preparing quiz");

        final Quiz quiz = this.quizDtoMapper.toEntity(quizDto);
        final Quiz savedQuiz = this.quizFacade.createQuiz(quiz, types, quantity);

        return this.quizDtoMapper.toDto(savedQuiz);
    }
}
