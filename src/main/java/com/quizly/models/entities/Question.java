package com.quizly.models.entities;

import com.quizly.enums.QuestionType;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;
}
