package com.quizly.models.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class QuestionAnswerDto {

    private Long questionId;

    private List<Character> answerOptions;

    @Size(min = 1, max = 255)
    private String answerText;
}