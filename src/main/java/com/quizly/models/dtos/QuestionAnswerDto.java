package com.quizly.models.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class QuestionAnswerDto {

    private Long questionId;

    private char answerPoint;

    @Size(min = 1, max = 255)
    private String answerText;
}