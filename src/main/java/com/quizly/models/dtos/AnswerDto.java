package com.quizly.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Long id;

    private char answerOption;

    @Size(min = 1, max = 255)
    private String text;

    private boolean correct;
}
