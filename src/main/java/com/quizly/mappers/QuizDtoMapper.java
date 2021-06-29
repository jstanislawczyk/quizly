package com.quizly.mappers;

import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.dtos.QuizDto;
import com.quizly.models.entities.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        return QuizDto
                .builder()
                    .id(quiz.getId())
                    .quizDurationInSeconds(quiz.getQuizDurationInSeconds())
                    .uniqueCode(quiz.getUniqueCode())
                    .startedAt(quiz.getStartedAt())
                    .finishedAt(quiz.getFinishedAt())
                    .correctQuestions(quiz.getCorrectQuestions())
                    .totalQuestions(quiz.getTotalQuestions())
                    .gainedPoints(quiz.getGainedPoints())
                    .totalPoints(quiz.getTotalPoints())
                .build();
    }

    public QuizDto toDtoWithQuestions(final Quiz quiz) {
        final QuizDto quizDto = this.toDto(quiz);
        final List<QuestionDto> questionDtos = this.questionDtoMapper.toDtoList(quiz.getQuestions());

        quizDto.setQuestions(questionDtos);

        return quizDto;
    }

    public QuizDto toDtoWithQuestionsWithoutCorrect(final Quiz quiz) {
        final QuizDto quizDto = this.toDto(quiz);
        final List<QuestionDto> questionDtos = this.questionDtoMapper.toDtoListWithAnswersWithoutCorrect(quiz.getQuestions());

        quizDto.setQuestions(questionDtos);

        return quizDto;
    }

    public QuizDto toDtoWithQuestionsWithAnswers(final Quiz quiz) {
        final  QuizDto quizDto = this.toDto(quiz);
        final List<QuestionDto> questionDtos = this.questionDtoMapper.toDtoListWithAnswers(quiz.getQuestions());

        quizDto.setQuestions(questionDtos);

        return quizDto;
    }

    public List<QuizDto> toDtoList(List<Quiz> quizzes) {
        return quizzes
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
