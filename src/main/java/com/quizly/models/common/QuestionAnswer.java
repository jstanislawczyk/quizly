package com.quizly.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionAnswer {

    private Long questionId;

    private char answerPoint;

    private String answerText;
}
