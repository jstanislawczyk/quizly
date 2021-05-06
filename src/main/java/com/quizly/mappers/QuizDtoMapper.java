package com.quizly.mappers;

import com.quizly.models.dtos.QuizDto;
import com.quizly.models.entities.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuizDtoMapper {

    public Quiz toEntity(final QuizDto quizDto) {
        return Quiz
                .builder()
                    .quizDurationInSeconds(quizDto.getQuizDurationInSeconds())
                .build();
    }

    public QuizDto toDto(final Quiz quiz) {
        return QuizDto
                .builder()
                    .id(quiz.getId())
                    .quizDurationInSeconds(quiz.getQuizDurationInSeconds())
                    .uniqueCode(quiz.getUniqueCode())
                    .startedAt(quiz.getStartedAt())
                    .endedAt(quiz.getEndedAt())
                .build();
    }
}
