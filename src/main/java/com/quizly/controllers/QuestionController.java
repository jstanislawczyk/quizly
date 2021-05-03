package com.quizly.controllers;

import com.quizly.enums.QuestionType;
import com.quizly.facades.QuestionFacade;
import com.quizly.mappers.AnswerDtoMapper;
import com.quizly.mappers.QuestionDtoMapper;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/questions")
@Validated
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionFacade questionFacade;
    private final QuestionDtoMapper questionDtoMapper;
    private final AnswerDtoMapper answerDtoMapper;

    @GetMapping
    public List<QuestionDto> getQuestions(
        @RequestParam(defaultValue = "SINGLE_CHOICE") final List<QuestionType> types,
        @RequestParam(defaultValue = "10") @Min(1) final int quantity
    ) {
        log.info("Fetching questions list");

        final List<Question> questions = this.questionFacade.getQuestionsList(types, quantity);

        return questionDtoMapper.toDtoList(questions);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public QuestionDto saveQuestion(@RequestBody @Valid final QuestionDto questionDto) {
        log.info("Saving new question with answers");

        final Question newQuestion = this.questionDtoMapper.toEntity(questionDto);
        final List<Answer> newAnswers = this.answerDtoMapper.toEntityList(questionDto.getAnswers());
        final Question savedQuestion = this.questionFacade.saveQuestionWithAnswers(newQuestion, newAnswers);

        return this.questionDtoMapper.toDtoWithAnswers(savedQuestion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteQuestion(@PathVariable final Long id) {
        log.info("Deleting question with id=" + id);

        this.questionFacade.deleteQuestionById(id);
    }
}
