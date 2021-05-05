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

    public Question toEntity(QuestionDto questionDto) {
        return Question
                .builder()
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

    public QuestionDto toDtoWithAnswers(Question question) {
        final QuestionDto questionDto = this.toDto(question);
        final List<AnswerDto> answersDtos = this.answerDtoMapper.toDtoList(question.getAnswers());

        questionDto.setAnswers(answersDtos);

        return questionDto;
    }

    public List<QuestionDto> toDtoList(List<Question> questions) {
        return questions
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
