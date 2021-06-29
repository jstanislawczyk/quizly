package com.quizly.models.dtos;

import com.quizly.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private Long id;

    @Min(value = 0, message = "Question must have minimum {value} points")
    @Max(value = 100, message = "Question must have at maximum {value} points")
    @NotNull
    private int points;

    @NotEmpty(message = "Question cannot be empty")
    @Size(min = 10, max = 510, message = "Question must have size between {min} and {max} letters")
    private String text;

    @Size(min = 10, max = 255, message = "Question photo URL must have size between {min} and {max} letters")
    private String photoUrl;

    @NotNull(message = "Question type cannot be null")
    private QuestionType questionType;

    private List<AnswerDto> answers;
}
