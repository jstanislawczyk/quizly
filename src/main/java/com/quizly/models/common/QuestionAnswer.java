package com.quizly.models.common;

import com.quizly.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionAnswer {

    private Long questionId;

    private QuestionType questionType;

    private boolean isCorrect;

    private int points;

    private List<Character> answerOptions;

    private List<Character> correctAnswerOptions;

    private String answerText;
}
