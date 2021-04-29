package com.quizly.mappers;

import com.quizly.models.dtos.AnswerDto;
import com.quizly.models.entities.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerDtoMapper {

    public Answer toEntity(AnswerDto answerDto) {
        return Answer
                .builder()
                    .point(answerDto.getPoint())
                    .text(answerDto.getText())
                    .correct(answerDto.isCorrect())
                .build();
    }

    public AnswerDto toDto(Answer answer) {
        return AnswerDto
                .builder()
                    .id(answer.getId())
                    .point(answer.getPoint())
                    .text(answer.getText())
                    .correct(answer.isCorrect())
                .build();
    }

    public List<Answer> toEntityList(List<AnswerDto> answerDtoList) {
        return answerDtoList
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<AnswerDto> toDtoList(List<Answer> answerList) {
        return answerList
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
