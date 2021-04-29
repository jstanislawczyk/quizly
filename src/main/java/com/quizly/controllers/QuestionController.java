package com.quizly.controllers;

import com.quizly.facades.QuestionFacade;
import com.quizly.mappers.AnswerDtoMapper;
import com.quizly.mappers.QuestionDtoMapper;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionFacade questionFacade;
    private final QuestionDtoMapper questionDtoMapper;
    private final AnswerDtoMapper answerDtoMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public QuestionDto saveQuestion(@RequestBody @Valid final QuestionDto questionDto) {
        log.info("Saving new question with answers");

        final Question newQuestion = this.questionDtoMapper.toEntity(questionDto);
        final List<Answer> newAnswers = this.answerDtoMapper.toEntityList(questionDto.getAnswers());
        final Question savedQuestion = this.questionFacade.saveQuestionWithAnswers(newQuestion, newAnswers);

        return this.questionDtoMapper.toDtoWithAnswers(savedQuestion);
    }
}