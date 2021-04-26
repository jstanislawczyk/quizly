package com.quizly.controllers;

import com.quizly.mappers.QuestionDtoMapper;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionDtoMapper questionDtoMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public QuestionDto saveQuestion(@RequestBody @Valid QuestionDto questionDto) {
        final Question question = this.questionDtoMapper.toEntity(questionDto);

        return this.questionDtoMapper.toDto(question);
    }
}
