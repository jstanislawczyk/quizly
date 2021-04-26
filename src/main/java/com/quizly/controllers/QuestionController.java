package com.quizly.controllers;

import com.quizly.mappers.QuestionDtoMapper;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Question;
import com.quizly.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionDtoMapper questionDtoMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public QuestionDto saveQuestion(@RequestBody @Valid QuestionDto questionDto) {
        final Question newQuestion = this.questionDtoMapper.toEntity(questionDto);
        final Question savedQuestion = this.questionService.saveQuestion(newQuestion);

        return this.questionDtoMapper.toDto(savedQuestion);
    }
}
