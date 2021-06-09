package com.quizly.mappers;

import com.quizly.models.dtos.AnswerDto;
import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Answer;
import com.quizly.models.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionDtoMapper {

    private final AnswerDtoMapper answerDtoMapper;

    public Question toEntity(final QuestionDto questionDto) {
        return Question
                .builder()
                    .points(questionDto.getPoints())
                    .text(questionDto.getText())
                    .photoUrl(questionDto.getPhotoUrl())
                    .questionType(questionDto.getQuestionType())
                .build();
    }

    public QuestionDto toDto(final Question question) {
        return QuestionDto
                .builder()
                    .id(question.getId())
                    .points(question.getPoints())
                    .text(question.getText())
                    .photoUrl(question.getPhotoUrl())
                    .questionType(question.getQuestionType())
                .build();
    }

    public QuestionDto toDtoWithAnswers(final Question question) {
        final QuestionDto questionDto = this.toDto(question);
        final List<AnswerDto> answersDtos = this.answerDtoMapper.toDtoList(question.getAnswers());

        questionDto.setAnswers(answersDtos);

        return questionDto;
    }

    public List<QuestionDto> toDtoList(final List<Question> questions) {
        return questions
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> toDtoListWithAnswers(final List<Question> questions) {
        return questions
                .stream()
                .map(this::toDtoWithAnswers)
                .collect(Collectors.toList());
    }
}
