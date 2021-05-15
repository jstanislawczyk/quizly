package com.quizly.mappers;

import com.quizly.models.common.QuestionAnswer;
import com.quizly.models.dtos.QuestionAnswerDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionAnswerMapper {

    public QuestionAnswer toModel(final QuestionAnswerDto questionAnswerDto) {
        return QuestionAnswer
                .builder()
                    .questionId(questionAnswerDto.getQuestionId())
                    .answerPoint(questionAnswerDto.getAnswerPoint())
                    .answerText(questionAnswerDto.getAnswerText())
                .build();
    }

    public List<QuestionAnswer> toModelList(final List<QuestionAnswerDto> questionAnswerDtos) {
        return questionAnswerDtos
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
