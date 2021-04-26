package com.quizly.mappers;

import com.quizly.models.dtos.QuestionDto;
import com.quizly.models.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDtoMapper {

    private final AnswerDtoMapper answerDtoMapper;

    public Question toEntity(QuestionDto questionDto) {
        return Question
                .builder()
                    .id(questionDto.getId())
                    .text(questionDto.getText())
                    .photoUrl(questionDto.getPhotoUrl())
                    .questionType(questionDto.getQuestionType())
                .build();
    }

    public QuestionDto toDto(Question question) {
        return QuestionDto
                .builder()
                    .id(question.getId())
                    .text(question.getText())
                    .photoUrl(question.getPhotoUrl())
                    .questionType(question.getQuestionType())
                .build();
    }
}
