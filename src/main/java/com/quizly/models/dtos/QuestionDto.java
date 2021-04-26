package com.quizly.models.dtos;

import com.quizly.enums.QuestionType;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class QuestionDto {

    private Long id;

    @Size(min = 10, max = 510)
    private String text;

    @Size(min = 10, max = 255)
    private String photoUrl;

    private QuestionType questionType;

    private List<AnswerDto> answers;
}
