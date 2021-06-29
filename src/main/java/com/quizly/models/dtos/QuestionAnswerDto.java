package com.quizly.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerDto {

    private Long questionId;

    private List<Character> answerOptions;

    @Size(min = 1, max = 255)
    private String answerText;
}