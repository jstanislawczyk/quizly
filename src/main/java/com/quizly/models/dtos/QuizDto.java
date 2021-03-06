package com.quizly.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {

    private Long id;

    private String uniqueCode;

    @Min(0)
    private int quizDurationInSeconds;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private int correctQuestions;

    private int totalQuestions;

    private int gainedPoints;

    private int totalPoints;

    private List<QuestionDto> questions;
}
