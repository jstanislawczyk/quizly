package com.quizly.models.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@Builder
public class QuizDto {

    private Long id;

    private String uniqueCode;

    @Min(0)
    private int quizDurationInSeconds;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;
}
