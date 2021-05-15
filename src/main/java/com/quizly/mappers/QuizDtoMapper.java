package com.quizly.mappers;

import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.dtos.QuizDto;
import com.quizly.models.entities.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizDtoMapper {

    private final QuestionDtoMapper questionDtoMapper;

    public Quiz toEntity(final QuizDto quizDto) {
        return Quiz
                .builder()
                    .quizDurationInSeconds(quizDto.getQuizDurationInSeconds())
                .build();
    }

    public QuizDto toDto(final Quiz quiz) {
        final List<QuestionDto> questionDtos = this.questionDtoMapper.toDtoList(quiz.getQuestions());

        return QuizDto
                .builder()
                    .id(quiz.getId())
                    .quizDurationInSeconds(quiz.getQuizDurationInSeconds())
                    .uniqueCode(quiz.getUniqueCode())
                    .startedAt(quiz.getStartedAt())
                    .finishedAt(quiz.getFinishedAt())
                    .correctQuestions(quiz.getCorrectQuestions())
                    .totalQuestions(quiz.getTotalQuestions())
                    .questions(questionDtos)
                .build();
    }

    public QuizDto toDtoWithAnswers(final Quiz quiz) {
        final List<QuestionDto> questionDtos = this.questionDtoMapper.toDtoListWithAnswers(quiz.getQuestions());

        return QuizDto
                .builder()
                    .id(quiz.getId())
                    .quizDurationInSeconds(quiz.getQuizDurationInSeconds())
                    .uniqueCode(quiz.getUniqueCode())
                    .startedAt(quiz.getStartedAt())
                    .finishedAt(quiz.getFinishedAt())
                    .correctQuestions(quiz.getCorrectQuestions())
                    .totalQuestions(quiz.getTotalQuestions())
                    .questions(questionDtos)
                .build();
    }
}
