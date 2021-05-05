package com.quizly.models.dtos;

import com.quizly.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class QuestionDto {

    private Long id;

    @NotEmpty(message = "Question cannot be empty")
    @Size(min = 10, max = 510, message = "Question must have size between {min} and {max} letters")
    private String text;

    @Size(min = 10, max = 255, message = "Question photo URL must have size between {min} and {max} letters")
    private String photoUrl;

    @NotNull(message = "Question type cannot be null")
    private QuestionType questionType;

    private List<AnswerDto> answers;
}
